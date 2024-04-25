package com.yandemelo.monitorias.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.exceptions.MonitoriaExistenteException;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class MonitoriaService {
    
    @Autowired
    private MonitoriaRepository repository;
    @Autowired
    private AuthorizationService service;
    
    @Transactional(readOnly = true)
    public Page<ConsultarMonitoriasDTO> consultarMonitoriasDisponiveis (Pageable pageable){
        Page<Monitoria> monitorias = repository.consultarMonitoriasDisponiveis(pageable);
        return monitorias.map(x -> new ConsultarMonitoriasDTO(x));
    }

    @Transactional
    public AbrirMonitoriaDTO ofertarMonitoria(AbrirMonitoriaDTO dto){
        User user = service.authenticated();
        Monitoria verificarMonitoria = repository.verificarMonitoriaExistente(user.getId(), dto.getDisciplina(), dto.getSemestre(), dto.getCurso());
        if (verificarMonitoria != null) {
            throw new MonitoriaExistenteException("Esta monitoria já está em aberto.");
        } else {
            Monitoria monitoria = new Monitoria();
            monitoria.setCurso(dto.getCurso());
            monitoria.setDataCadastro(LocalDate.now());
            monitoria.setDisciplina(dto.getDisciplina());
            monitoria.setSemestre(dto.getSemestre());
            monitoria.setStatus(StatusMonitoria.DISPONIVEL);
            monitoria.setUltimaAtualizacao(LocalDate.now());
            monitoria.setMonitorId(null);
            monitoria.setProfessorId(user);
            repository.save(monitoria);
            return new AbrirMonitoriaDTO(monitoria);
        }
    }

}
