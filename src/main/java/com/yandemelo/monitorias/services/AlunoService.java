package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.BuscarStatusCandidaturaDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.CandidatarAlunoDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.StatusMonitoriaDTO;
import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.exceptions.AlunoCandidatado;
import com.yandemelo.monitorias.exceptions.AlunoNaoCandidatado;
import com.yandemelo.monitorias.exceptions.CursosDiferentes;
import com.yandemelo.monitorias.exceptions.InvalidFileException;
import com.yandemelo.monitorias.repositories.ArquivoRepository;
import com.yandemelo.monitorias.repositories.CandidatoMonitoriaRepository;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class AlunoService {

    @Autowired
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private AuthorizationService userService;

    @Transactional(readOnly = true)
    public BuscarStatusCandidaturaDTO statusCandidatura (){
        User user = userService.authenticated();
        CandidatoMonitoria candidato = candidatoMonitoriaRepository.verInscricao(user);
        if (candidato == null) {
            throw new AlunoNaoCandidatado("Você não está candidatado em nenhuma monitoria.");
        }
        StatusMonitoriaDTO monitoria = new StatusMonitoriaDTO(candidato.getMonitoriaId());
        return new BuscarStatusCandidaturaDTO(candidato, monitoria);
    }

    @Transactional(readOnly = true)
    public ConsultarMonitoriasDTO statusMonitoria() {
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.buscarPorCandidato(user.getId());
        if (monitoria == null) {
            throw new AlunoNaoCandidatado("Você ainda não foi aceito em nenhuma monitoria.");
        }
        return new ConsultarMonitoriasDTO(monitoria);
     }

     @Transactional
    public CandidatarAlunoDTO candidatarAluno(Long monitoriaId, MultipartFile historicoEscolar){
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.getReferenceById(monitoriaId);
        if (user.getCurso() != monitoria.getCurso()) {
            throw new CursosDiferentes("Esta monitoria não está disponível para o seu curso.");
        }
        if (!historicoEscolar.getContentType().equals("application/pdf")) {
            throw new InvalidFileException("Tipo de arquivo inválido, apenas PDF's.");
        } 
        
        Arquivo arquivoParaSalvar = arquivoRepository.getArquivoPorIdAluno(user.getId());
        if (arquivoParaSalvar != null) {
            arquivoParaSalvar.setId(arquivoParaSalvar.getId());
            salvarArquivo(arquivoParaSalvar, historicoEscolar, user);
            arquivoRepository.save(arquivoParaSalvar);
        } else {
            arquivoParaSalvar = new Arquivo();
            salvarArquivo(arquivoParaSalvar, historicoEscolar, user);
            arquivoRepository.save(arquivoParaSalvar);
        }
            CandidatoMonitoria candidato = candidatoMonitoriaRepository.verInscricao(user);
            if (candidato == null) {
                candidato = new CandidatoMonitoria();
                salvarCandidato(candidato, user, monitoria, arquivoParaSalvar);
                candidatoMonitoriaRepository.save(candidato);
            } else {
                throw new AlunoCandidatado("Aluno já inscrito em outra monitoria.");
            }

            StatusMonitoriaDTO dto = new StatusMonitoriaDTO(monitoria);
            return new CandidatarAlunoDTO(user, dto);
    }

    public void salvarArquivo(Arquivo arquivoParaSalvar, MultipartFile historicoEscolar, User user){
        try {
            arquivoParaSalvar.setConteudo(historicoEscolar.getBytes());
            arquivoParaSalvar.setDataCadastro(LocalDate.now());
            arquivoParaSalvar.setIdAluno(user.getId());
            arquivoParaSalvar.setNomeArquivo(historicoEscolar.getOriginalFilename());
            arquivoParaSalvar.setUltimaAtualizacao(LocalDate.now());
        } catch (Exception e) {
            
        }

    }

    public void salvarCandidato (CandidatoMonitoria candidato, User user, Monitoria monitoria, Arquivo arquivoParaSalvar){
        try {
            candidato.setDataCadastro(LocalDate.now());
            candidato.setDataSolicitacao(LocalDate.now());
            candidato.setStatusCandidatura(StatusCandidatura.AGUARDANDO_APROVACAO);
            candidato.setUltimaAtualizacao(LocalDate.now());
            candidato.setAlunoId(user);
            candidato.setMonitoriaId(monitoria);
            candidato.setPdfHistoricoEscolar(arquivoParaSalvar);
        } catch (Exception e) {

        }
    }

}
