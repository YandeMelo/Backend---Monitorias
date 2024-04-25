package com.yandemelo.monitorias.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.yandemelo.monitorias.dto.AbrirMonitoriaDTO;
import com.yandemelo.monitorias.dto.CandidatoMonitoriaDTO;
import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.exceptions.InvalidFileException;
import com.yandemelo.monitorias.services.MonitoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/monitorias")
public class MonitoriaController {
    
    @Autowired
    private MonitoriaService service;

    @GetMapping(value = "/disponiveis")
    public ResponseEntity<Page<ConsultarMonitoriasDTO>> consultarMonitoriasDisponiveis(Pageable pageable){
        Page<ConsultarMonitoriasDTO> dto = service.consultarMonitoriasDisponiveis(pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/abrir")
    public ResponseEntity<AbrirMonitoriaDTO> ofertarMonitoria(@Valid @RequestBody AbrirMonitoriaDTO dto){
        AbrirMonitoriaDTO monitoriaDTO = service.ofertarMonitoria(dto);
        if (monitoriaDTO != null) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(monitoriaDTO.getId()).toUri();
            return ResponseEntity.created(uri).body(monitoriaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/candidatar/{idMonitoria}")
    public ResponseEntity<CandidatoMonitoriaDTO> candidatarAlunoMonitoria(@PathVariable Long idMonitoria, @Valid @RequestBody MultipartFile historicoEscolar){
        if (historicoEscolar.isEmpty()){
            throw new InvalidFileException("Histórico Escolar (PDF) necessário para a candidatura.");
        }
        CandidatoMonitoriaDTO candidatoMonitoria = service.candidatarAluno(idMonitoria, historicoEscolar);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(candidatoMonitoria.getAlunoId(), candidatoMonitoria.getMonitoriaId()).toUri();
        return ResponseEntity.created(uri).body(candidatoMonitoria);
    }

}
