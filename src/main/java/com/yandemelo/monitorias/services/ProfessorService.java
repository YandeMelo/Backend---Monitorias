package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class ProfessorService {

    @Autowired
    private AuthorizationService userService;
    @Autowired
    private MonitoriaRepository monitoriaRepository;

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
