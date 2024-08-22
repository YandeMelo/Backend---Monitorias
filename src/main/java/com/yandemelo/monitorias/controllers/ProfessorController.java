package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yandemelo.monitorias.dto.AvaliarCandidatoDTO;
import com.yandemelo.monitorias.dto.AvaliarMonitoriaDTO;
import com.yandemelo.monitorias.dto.ConsultarCandidatosDTO;
import com.yandemelo.monitorias.dto.MonitoriaDTO;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.services.ProfessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @Operation(summary = "Monitorias abertas pelo professor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
    })
    @GetMapping("/monitorias")
    public ResponseEntity<Page<MonitoriaDTO>> minhasMonitorias (Pageable pageable){
        Page<MonitoriaDTO> monitorias = professorService.minhasMonitorias(pageable);
        return ResponseEntity.ok(monitorias);
    }

    @Operation(summary = "Verificar candidatos da monitoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping("candidatos/{idMonitoria}")
    public ResponseEntity<Page<ConsultarCandidatosDTO>> consultarCandidatos(@PathVariable Long idMonitoria, Pageable pageable){
        Page<ConsultarCandidatosDTO> candidatos = professorService.consultarCandidatos(idMonitoria, pageable);
        return ResponseEntity.ok(candidatos);
    }
    
    @Operation(summary = "Verificar perfil do aluno candidatado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @GetMapping("avaliar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> avaliarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.avaliarCandidato(idMonitoria, idAluno);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Verificar monitoria e monitor atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @GetMapping("/monitoria/avaliar/{idMonitoria}")
    public ResponseEntity<AvaliarMonitoriaDTO> avaliarMonitoria(@PathVariable Long idMonitoria){
        AvaliarMonitoriaDTO dto = professorService.avaliarMonitoria(idMonitoria);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Baixar histórico escolar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @GetMapping("historico/{idArquivo}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long idArquivo) {
        return professorService.baixarHistoricoEscolar(idArquivo);
    }

    @Operation(summary = "Recusar candidato na monitoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content())
    })
    @PutMapping("recusar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> recusarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.aprovarOuRecusarCandidatura(idMonitoria, idAluno, StatusCandidatura.RECUSADO);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Aprovar candidato na monitoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Not Found", content = @Content())
    })
    @PutMapping("aprovar/{idAluno}/{idMonitoria}")
    public ResponseEntity<AvaliarCandidatoDTO> aprovarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.aprovarOuRecusarCandidatura(idMonitoria, idAluno, StatusCandidatura.APROVADO);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Alterar Relatorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ok"),
        @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Not Found", content = @Content())
    })
    @PutMapping("/alterarRelatorio/{idAluno}/{idArquivo}/{idMonitoria}")
    public ResponseEntity<ByteArrayResource> alterarRelatorio(@PathVariable Long idAluno, @PathVariable Long idArquivo, @PathVariable Long idMonitoria, @Valid @RequestBody MultipartFile relatorio){
        return professorService.alterarRelatorio(idAluno, idArquivo, idMonitoria, relatorio);
    }

}
