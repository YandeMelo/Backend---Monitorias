package com.yandemelo.monitorias.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.dto.AvaliarCandidatoDTO;
import com.yandemelo.monitorias.dto.ConsultarCandidatosDTO;
import com.yandemelo.monitorias.dto.MonitoriaDTO;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.services.ProfessorService;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/monitorias")
    public ResponseEntity<List<MonitoriaDTO>> minhasMonitorias (){
        List<MonitoriaDTO> monitorias = professorService.minhasMonitorias();
        return ResponseEntity.ok(monitorias);
    }

    @GetMapping("candidatos/{idMonitoria}")
    public ResponseEntity<List<ConsultarCandidatosDTO>> consultarCandidatos(@PathVariable Long idMonitoria){
        List<ConsultarCandidatosDTO> candidatos = professorService.consultarCandidatos(idMonitoria);
        return ResponseEntity.ok(candidatos);
    }
    
    @GetMapping("avaliar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> avaliarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.avaliarCandidato(idMonitoria, idAluno);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("historico/{idAluno}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long idAluno) {
        return professorService.baixarHistoricoEscolar(idAluno);
    }

    @PutMapping("recusar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> recusarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.aprovarOuRecusarCandidatura(idMonitoria, idAluno, StatusCandidatura.RECUSADO);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("aprovar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> aprovarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.aprovarOuRecusarCandidatura(idMonitoria, idAluno, StatusCandidatura.APROVADO);
        return ResponseEntity.ok(dto);
    }

}
