package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.BuscarStatusCandidaturaDTO;
import com.yandemelo.monitorias.services.AlunoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Operation(summary = "Verificar status da candidatura")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
    })
    @GetMapping(value = "/inscricao")
    public ResponseEntity<BuscarStatusCandidaturaDTO> statusCandidatura(){
        BuscarStatusCandidaturaDTO dto = alunoService.statusCandidatura();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Verificar status da monitoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
    })
    @GetMapping(value = "/monitoria")
    public ResponseEntity<ConsultarMonitoriasDTO> statusMonitoria(){
        ConsultarMonitoriasDTO dto = alunoService.statusMonitoria();
        return ResponseEntity.ok(dto);
    }
}
