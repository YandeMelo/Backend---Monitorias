package com.yandemelo.monitorias.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.dto.AvaliarCandidatoDTO;
import com.yandemelo.monitorias.dto.ConsultarCandidatosDTO;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.AlunoNaoCandidatado;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.exceptions.MonitoriaProfessorDiferente;
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
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;

    @Transactional
    public AbrirMonitoriaDTO ofertarMonitoria(AbrirMonitoriaDTO dto){
        User user = userService.authenticated();
        Monitoria verificarMonitoria = monitoriaRepository.verificarMonitoriaExistente(user.getId(), dto.getDisciplina(), dto.getSemestre(), user.getCurso());
        if (verificarMonitoria != null) {
            throw new MonitoriaExistenteException("Esta monitoria já está em aberto.");
        } else {
            Monitoria monitoria = new Monitoria();
            salvarMonitoria(dto, user, monitoria);
            monitoriaRepository.save(monitoria);
            return new AbrirMonitoriaDTO(monitoria, user);
        }
    }

    @Transactional(readOnly = true)
    public List<ConsultarCandidatosDTO> consultarCandidatos(Long idMonitoria){
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new MonitoriaExistenteException("Esta monitoria não existe."));
        User user = userService.authenticated();
        
        if (!monitoria.getProfessorId().getId().equals(user.getId())) {
            throw new MonitoriaProfessorDiferente("Esta monitoria pertence a outro professor.");
        }
        List<CandidatoMonitoria> candidato = candidatoMonitoriaRepository.consultarCandidatos(idMonitoria);
        return candidato.stream().map(x -> new ConsultarCandidatosDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvaliarCandidatoDTO avaliarCandidato(Long idMonitoria, Long idAluno){
        User aluno = userRepository.findById(idAluno).orElseThrow(() -> new AlunoNaoCandidatado("Aluno não encontrado."));
        Monitoria monitoria = monitoriaRepository.findById(idMonitoria).orElseThrow(() -> new MonitoriaExistenteException("Monitoria não encontrada."));
        CandidatoMonitoria inscricao = candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoria);
        if (inscricao == null) {
            throw new AlunoNaoCandidatado("Este aluno não pertence a essa monitoria.");
        }
        return new AvaliarCandidatoDTO(aluno, inscricao);
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
