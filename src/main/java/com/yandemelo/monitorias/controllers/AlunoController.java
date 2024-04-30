package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.dto.candidaturaAluno.BuscarStatusCandidatura;
import com.yandemelo.monitorias.services.AlunoService;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping(value = "/status")
    public ResponseEntity<BuscarStatusCandidatura> statusCandidatura(){
        BuscarStatusCandidatura dto = alunoService.statusCandidatura();
        return ResponseEntity.ok(dto);
    }
}
