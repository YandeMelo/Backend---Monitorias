package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.BuscarStatusCandidaturaDTO;
import com.yandemelo.monitorias.services.AlunoService;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping(value = "/inscricao")
    public ResponseEntity<BuscarStatusCandidaturaDTO> statusCandidatura(){
        BuscarStatusCandidaturaDTO dto = alunoService.statusCandidatura();
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/monitoria")
    public ResponseEntity<ConsultarMonitoriasDTO> statusMonitoria(){
        ConsultarMonitoriasDTO dto = alunoService.statusMonitoria();
        return ResponseEntity.ok(dto);
    }
}
