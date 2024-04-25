package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.dto.CandidatoMonitoriaDTO;
import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.AlunoCandidatado;
import com.yandemelo.monitorias.exceptions.InvalidFileException;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.repositories.ArquivoRepository;
import com.yandemelo.monitorias.repositories.CandidatoMonitoriaRepository;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class MonitoriaService {
    
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;

    @Autowired
    private AuthorizationService userService;
    
    @Transactional(readOnly = true)
    public Page<ConsultarMonitoriasDTO> consultarMonitoriasDisponiveis (Pageable pageable){
        Page<Monitoria> monitorias = monitoriaRepository.consultarMonitoriasDisponiveis(pageable);
        return monitorias.map(x -> new ConsultarMonitoriasDTO(x));
    }

    @Transactional
    public AbrirMonitoriaDTO ofertarMonitoria(AbrirMonitoriaDTO dto){
        User user = userService.authenticated();
        Monitoria verificarMonitoria = monitoriaRepository.verificarMonitoriaExistente(user.getId(), dto.getDisciplina(), dto.getSemestre(), dto.getCurso());
        if (verificarMonitoria != null) {
            throw new MonitoriaExistenteException("Esta monitoria já está em aberto.");
        } else {
            Monitoria monitoria = new Monitoria();
            salvarMonitoria(dto, user, monitoria);
            monitoriaRepository.save(monitoria);
            return new AbrirMonitoriaDTO(monitoria);
        }
    }

    @Transactional
    public CandidatoMonitoriaDTO candidatarAluno(Long monitoriaId, MultipartFile historicoEscolar){
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.getReferenceById(monitoriaId);
        Arquivo arquivoParaSalvar = arquivoRepository.getArquivoPorIdAluno(user.getId());

        if (!historicoEscolar.getContentType().equals("application/pdf")) {
            throw new InvalidFileException("Tipo de arquivo inválido, apenas PDF's.");
        } 

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

            return new CandidatoMonitoriaDTO(user, monitoria, arquivoParaSalvar.getId());
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

    public void salvarMonitoria(AbrirMonitoriaDTO dto, User user, Monitoria monitoria){
        try {
            monitoria.setCurso(dto.getCurso());
            monitoria.setDataCadastro(LocalDate.now());
            monitoria.setDisciplina(dto.getDisciplina());
            monitoria.setSemestre(dto.getSemestre());
            monitoria.setStatus(StatusMonitoria.DISPONIVEL);
            monitoria.setUltimaAtualizacao(LocalDate.now());
            monitoria.setMonitorId(null);
            monitoria.setProfessorId(user);
        } catch (Exception e) {
            
        }    
    }

}
