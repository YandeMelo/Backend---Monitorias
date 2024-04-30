package com.yandemelo.monitorias.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class MonitoriaService {
    
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private AuthorizationService userService;
    
    @Transactional(readOnly = true)
    public Page<ConsultarMonitoriasDTO> consultarMonitoriasDisponiveis (Pageable pageable){
        User user = userService.authenticated();
        Page<Monitoria> monitorias = monitoriaRepository.consultarMonitoriasDisponiveis(user.getCurso(), pageable);
        return monitorias.map(x -> new ConsultarMonitoriasDTO(x));
    }

}
