package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbrirMonitoriaDTO {
    @JsonIgnore
    private Long id;

    @NotBlank
    private String disciplina;
    
    @Enumerated(EnumType.STRING)
    private CursosExistentes curso;
    @NotBlank
    private String semestre;

    private Long professorId;

    @Enumerated(EnumType.STRING)
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

    public AbrirMonitoriaDTO(Monitoria monitoria, User user) {
        id = monitoria.getId();
        disciplina = monitoria.getDisciplina();
        curso = user.getCurso();
        semestre = monitoria.getSemestre();
        professorId = monitoria.getProfessorId().getId();
        status = monitoria.getStatus();
        dataCadastro = monitoria.getDataCadastro();
        ultimaAtualizacao = monitoria.getUltimaAtualizacao();
    }

}
