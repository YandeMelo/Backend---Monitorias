package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.dto.UserDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.BuscarStatusCandidaturaDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.CandidatarAlunoDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.StatusMonitoriaDTO;
import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.exceptions.AlunoCandidaturaException;
import com.yandemelo.monitorias.exceptions.BadRequestException;
import com.yandemelo.monitorias.exceptions.MethodArgumentNotValidException;
import com.yandemelo.monitorias.exceptions.ResourceNotFoundException;
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
    public BuscarStatusCandidaturaDTO statusCandidatura() {
        User user = userService.authenticated();
        CandidatoMonitoria candidato = candidatoMonitoriaRepository.verInscricao(user);
        if (candidato == null) {
            throw new AlunoCandidaturaException("Você não está candidatado em nenhuma monitoria.");
        }
        StatusMonitoriaDTO monitoria = new StatusMonitoriaDTO(candidato.getMonitoriaId());
        return new BuscarStatusCandidaturaDTO(candidato, monitoria);
    }

    @Transactional(readOnly = true)
    public ConsultarMonitoriasDTO statusMonitoria() {
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.buscarPorCandidato(user.getId());
        if (monitoria == null) {
            throw new AlunoCandidaturaException("Você ainda não foi aceito em nenhuma monitoria.");
        }
        return new ConsultarMonitoriasDTO(monitoria);
    }

    @Transactional
    public CandidatarAlunoDTO candidatarAluno(Long monitoriaId, MultipartFile historicoEscolar) {
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.findById(monitoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitoria não encontrada."));

        if (user.getCurso() != monitoria.getCurso()) {
            throw new BadRequestException("Esta monitoria não está disponível para o seu curso.");
        }
        if (!historicoEscolar.getContentType().equals("application/pdf")) {
            throw new MethodArgumentNotValidException("Tipo de arquivo inválido, apenas PDF's.");
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
            throw new AlunoCandidaturaException("Aluno já inscrito em outra monitoria.");
        }

        StatusMonitoriaDTO dto = new StatusMonitoriaDTO(monitoria);
        return new CandidatarAlunoDTO(user, dto);
    }

    @Transactional
    public ResponseEntity<ByteArrayResource> adicionarRelatorio(MultipartFile relatorio){
        try {
            User user = userService.authenticated();
            Arquivo arquivo = new Arquivo();
            salvarArquivo(arquivo, relatorio, user);
            arquivoRepository.save(arquivo);
            CandidatoMonitoria candidatura = candidatoMonitoriaRepository.consultarAlunoCandidatado(user.getId());
            candidatura.setRelatorioMonitoria(arquivo);
            candidatoMonitoriaRepository.save(candidatura);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getNomeArquivo());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(arquivo.getConteudo().length));
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            throw new BadRequestException("Arquivo já existente.");
        }
    }

    @Transactional
    public UserDTO getUser(){
        User user = userService.authenticated();
        return new UserDTO(user);
    }

    public void salvarArquivo(Arquivo arquivoParaSalvar, MultipartFile historicoEscolar, User user) {
        try {
            arquivoParaSalvar.setConteudo(historicoEscolar.getBytes());
            arquivoParaSalvar.setDataCadastro(LocalDate.now());
            arquivoParaSalvar.setIdAluno(user.getId());
            arquivoParaSalvar.setNomeArquivo(historicoEscolar.getOriginalFilename());
            arquivoParaSalvar.setUltimaAtualizacao(LocalDate.now());
        } catch (Exception e) {

        }

    }

    public void salvarCandidato(CandidatoMonitoria candidato, User user, Monitoria monitoria,
            Arquivo arquivoParaSalvar) {
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
