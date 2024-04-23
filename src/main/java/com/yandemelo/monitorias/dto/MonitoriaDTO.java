package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.projections.MonitoriaProjection;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonitoriaDTO {

    private Long id;

    @NotBlank
    private String disciplina;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private CursosExistentes curso;
    @NotBlank
    private String semestre;
    
    private User professorId;
    private User monitorId;

    @Enumerated(EnumType.STRING)
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

    public MonitoriaDTO(MonitoriaProjection projection) {
        id = projection.getId();
        disciplina = projection.getDisciplina();
        curso = CursosExistentes.valueOf(projection.getCurso());
        semestre = projection.getSemestre();
        professorId = projection.getProfessorId();
        monitorId = projection.getMonitorId();
        status = StatusMonitoria.valueOf(projection.getStatus());
        dataCadastro = projection.getDataCadastro();
        ultimaAtualizacao = projection.getUltimaAtualizacao();
    }

    public MonitoriaDTO(Monitoria projection) {
        id = projection.getId();
        disciplina = projection.getDisciplina();
        curso = projection.getCurso();
        semestre = projection.getSemestre();
        professorId = projection.getProfessorId();
        monitorId = projection.getMonitorId();
        status = projection.getStatus();
        dataCadastro = projection.getDataCadastro();
        ultimaAtualizacao = projection.getUltimaAtualizacao();
    }

}
