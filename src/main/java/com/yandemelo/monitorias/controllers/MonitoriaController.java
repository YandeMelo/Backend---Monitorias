package com.yandemelo.monitorias.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.services.MonitoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/monitorias")
public class MonitoriaController {
    
    @Autowired
    private MonitoriaService service;

    @PostMapping
    public ResponseEntity<AbrirMonitoriaDTO> ofertarMonitoria(@Valid @RequestBody AbrirMonitoriaDTO dto){
        AbrirMonitoriaDTO monitoriaDTO = service.ofertarMonitoria(dto);
        if (monitoriaDTO != null) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(monitoriaDTO.getId()).toUri();
            return ResponseEntity.created(uri).body(monitoriaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
