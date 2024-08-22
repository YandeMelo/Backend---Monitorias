package com.yandemelo.monitorias.dto.candidaturaAluno;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusMonitoriaDTO {

    private CursosExistentes curso;
    private String disciplina;
    private String professor;
    private String semestre;
    private LocalDate ultimaAtualizacao;
    private StatusMonitoria statusMonitoria;

    public StatusMonitoriaDTO(Monitoria monitoria) {
        professor = monitoria.getProfessorId().getNome();
        disciplina = monitoria.getDisciplina();
        curso = monitoria.getCurso();
        semestre = monitoria.getSemestre();
        ultimaAtualizacao = monitoria.getUltimaAtualizacao();
        statusMonitoria = monitoria.getStatus();
        
    }

}
