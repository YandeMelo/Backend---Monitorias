package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yandemelo.monitorias.dto.MonitoriaDTO;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.projections.MonitoriaProjection;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

import jakarta.transaction.Transactional;

@Service
public class MonitoriaService {
    
    @Autowired
    private MonitoriaRepository repository;
    @Autowired
    private AuthorizationService service;
    
    @Transactional
    public MonitoriaDTO ofertarMonitoria(MonitoriaDTO dto){
        User user = service.authenticated();
        MonitoriaProjection verificarMonitoria = repository.verificarMonitoriaExistente(user.getId(), dto.getDisciplina(), dto.getSemestre(), dto.getCurso().toString());
        if (verificarMonitoria != null) {
            throw new MonitoriaExistenteException("Esta monitoria já está em aberto.");
        } else {
            Monitoria monitoria = new Monitoria();
            monitoria.setCurso(dto.getCurso());
            monitoria.setDataCadastro(LocalDate.now());
            monitoria.setDisciplina(dto.getDisciplina());
            monitoria.setSemestre(dto.getSemestre());
            monitoria.setStatus(StatusMonitoria.INSCRICAO);
            monitoria.setUltimaAtualizacao(LocalDate.now());
            monitoria.setMonitorId(null);
            monitoria.setProfessorId(user);
            repository.save(monitoria);
            return new MonitoriaDTO(monitoria);
        }
    }

}
