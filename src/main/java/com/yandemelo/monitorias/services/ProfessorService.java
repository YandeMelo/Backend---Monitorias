package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.dto.AvaliarCandidatoDTO;
import com.yandemelo.monitorias.dto.ConsultarCandidatosDTO;
import com.yandemelo.monitorias.dto.MonitoriaDTO;
import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.BadRequestException;
import com.yandemelo.monitorias.exceptions.ResourceNotFoundException;
import com.yandemelo.monitorias.repositories.ArquivoRepository;
import com.yandemelo.monitorias.repositories.CandidatoMonitoriaRepository;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class ProfessorService {

    @Autowired
    private AuthorizationService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;

    @Transactional
    public Page<MonitoriaDTO> minhasMonitorias(Pageable pageable){
        User user = userService.authenticated();
        Page<Monitoria> monitorias = monitoriaRepository.buscarPorProfessor(user.getId(), pageable);
        return monitorias.map(x -> new MonitoriaDTO(x));
    }

    @Transactional
    public MonitoriaDTO suspenderMonitoria(Long idMonitoria){
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new BadRequestException("Esta monitoria não existe."));
        monitoria.setStatus(StatusMonitoria.SUSPENSA);
        monitoria.setUltimaAtualizacao(LocalDate.now());
        monitoriaRepository.save(monitoria);
        return new MonitoriaDTO(monitoria);
    }

    @Transactional
    public AbrirMonitoriaDTO ofertarMonitoria(AbrirMonitoriaDTO dto){
        User user = userService.authenticated();
        Monitoria verificarMonitoria = monitoriaRepository.verificarMonitoriaExistente(user.getId(), dto.getDisciplina(), dto.getSemestre(), user.getCurso());
        if (verificarMonitoria != null) {
            throw new BadRequestException("Esta monitoria já está aberta.");
        } else {
            Monitoria monitoria = new Monitoria();
            salvarMonitoria(dto, user, monitoria);
            monitoriaRepository.save(monitoria);
            return new AbrirMonitoriaDTO(monitoria, user);
        }
    }

    @Transactional(readOnly = true)
    public Page<ConsultarCandidatosDTO> consultarCandidatos(Long idMonitoria, Pageable pageable){
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new BadRequestException("Esta monitoria não existe."));
        User user = userService.authenticated();
        
        if (!monitoria.getProfessorId().getId().equals(user.getId())) {
            throw new BadRequestException("Esta monitoria pertence a outro professor.");
        }
        Page<CandidatoMonitoria> candidato = candidatoMonitoriaRepository.consultarCandidatos(idMonitoria, pageable);
        return candidato.map(x -> new ConsultarCandidatosDTO(x));
    }

    @Transactional(readOnly = true)
    public AvaliarCandidatoDTO avaliarCandidato(Long idMonitoria, Long idAluno){
        User aluno = userRepository.findById(idAluno).orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado."));
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new BadRequestException("Monitoria não encontrada."));
        CandidatoMonitoria inscricao = candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoria);
        if (inscricao == null) {
            throw new ResourceNotFoundException("Este aluno não está inscrito nessa monitoria.");
        }
        return new AvaliarCandidatoDTO(aluno, inscricao);
    }

    @Transactional
    public AvaliarCandidatoDTO aprovarOuRecusarCandidatura(Long idMonitoria, Long idAluno, StatusCandidatura status){
        User aluno = userRepository.findById(idAluno).orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado."));
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new BadRequestException("Monitoria não encontrada."));
        CandidatoMonitoria inscricao = candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoria);
        if (inscricao == null) {
            throw new ResourceNotFoundException("Este aluno não está inscrito nessa monitoria.");
        }
        if (status == StatusCandidatura.APROVADO) {
            monitoria.setStatus(StatusMonitoria.ANDAMENTO);
            monitoria.setMonitorId(aluno);
        } else {
            monitoria.setStatus(StatusMonitoria.DISPONIVEL);
        }
        monitoria.setUltimaAtualizacao(LocalDate.now());
        monitoriaRepository.save(monitoria);
        inscricao.setStatusCandidatura(status);
        inscricao.setUltimaAtualizacao(LocalDate.now());
        candidatoMonitoriaRepository.save(inscricao);
        return new AvaliarCandidatoDTO(aluno, inscricao);
    }

    @Transactional
    public ResponseEntity<ByteArrayResource> baixarHistoricoEscolar(Long idAluno){
        Arquivo arquivo = arquivoRepository.getArquivoPorIdAluno(idAluno);
        if (arquivo == null) {
            throw new ResourceNotFoundException("Arquivo não encontrado.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getNomeArquivo());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(arquivo.getConteudo().length));
        return ResponseEntity.ok()
            .headers(headers)
            .body(new ByteArrayResource(arquivo.getConteudo()));
    }

    public void salvarMonitoria(AbrirMonitoriaDTO dto, User user, Monitoria monitoria){
        try {
            monitoria.setCurso(user.getCurso());
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
