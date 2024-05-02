package com.yandemelo.monitorias.dto;

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
public class MonitoriaDTO {
    
    private Long id;
    private String professorNome;
    private String monitorNome;
    private String disciplina;
    private CursosExistentes curso;
    private String semestre;
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;
    
    public MonitoriaDTO(Monitoria monitoria) {
        id = monitoria.getId();
        professorNome = monitoria.getProfessorId().getNome();

        if (monitoria.getMonitorId() == null) {
            monitorNome = null;
        } else {
            monitorNome = monitoria.getMonitorId().getNome();
        }
        
        disciplina = monitoria.getDisciplina();
        curso = monitoria.getCurso();
        semestre = monitoria.getSemestre();
        status = monitoria.getStatus();
        dataCadastro = monitoria.getDataCadastro();
        ultimaAtualizacao = monitoria.getUltimaAtualizacao();
    }

}
