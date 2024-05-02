package com.yandemelo.monitorias.controllers;

import java.net.URI;
import java.util.List;

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
import com.yandemelo.monitorias.dto.AvaliarCandidatoDTO;
import com.yandemelo.monitorias.dto.ConsultarCandidatosDTO;
import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.dto.candidaturaAluno.CandidatarAlunoDTO;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.exceptions.InvalidFileException;
import com.yandemelo.monitorias.services.AlunoService;
import com.yandemelo.monitorias.services.MonitoriaService;
import com.yandemelo.monitorias.services.ProfessorService;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/monitorias")
public class MonitoriaController {
    
    @Autowired
    private MonitoriaService monitoriaService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private AuthorizationService userService;
    @Autowired
    private AlunoService alunoService;

    @GetMapping(value = "/disponiveis")
    public ResponseEntity<Page<ConsultarMonitoriasDTO>> consultarMonitoriasDisponiveis(Pageable pageable){
        Page<ConsultarMonitoriasDTO> dto = monitoriaService.consultarMonitoriasDisponiveis(pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{idMonitoria}/candidatos")
    public ResponseEntity<List<ConsultarCandidatosDTO>> consultarCandidatos(@PathVariable Long idMonitoria){
        List<ConsultarCandidatosDTO> candidatos = professorService.consultarCandidatos(idMonitoria);
        return ResponseEntity.ok(candidatos);
    }

    @GetMapping("/{idMonitoria}/avaliar/{idAluno}")
    public ResponseEntity<AvaliarCandidatoDTO> avaliarCandidato(@PathVariable Long idMonitoria, @PathVariable Long idAluno){
        AvaliarCandidatoDTO dto = professorService.avaliarCandidato(idMonitoria, idAluno);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/abrir")
    public ResponseEntity<AbrirMonitoriaDTO> ofertarMonitoria(@Valid @RequestBody AbrirMonitoriaDTO dto){
        AbrirMonitoriaDTO monitoriaDTO = professorService.ofertarMonitoria(dto);
        if (monitoriaDTO != null) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(monitoriaDTO.getId()).toUri();
            return ResponseEntity.created(uri).body(monitoriaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/candidatar/{idMonitoria}")
    public ResponseEntity<CandidatarAlunoDTO> candidatarAlunoMonitoria(@PathVariable Long idMonitoria, @Valid @RequestBody MultipartFile historicoEscolar){
        if (historicoEscolar.isEmpty()){
            throw new InvalidFileException("Histórico Escolar (PDF) necessário para a candidatura.");
        }
        CandidatarAlunoDTO candidatoMonitoria = alunoService.candidatarAluno(idMonitoria, historicoEscolar);
        User user = userService.authenticated();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(candidatoMonitoria);
    }

}
