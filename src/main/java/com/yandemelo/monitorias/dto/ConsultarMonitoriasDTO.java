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
public class ConsultarMonitoriasDTO {
    
    private String professorNome;
    private String disciplina;
    private CursosExistentes curso;
    private String semestre;
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;
    
    public ConsultarMonitoriasDTO(Monitoria monitorias) {

        professorNome = monitorias.getProfessorId().getNome();
        disciplina = monitorias.getDisciplina();
        curso = monitorias.getCurso();
        semestre = monitorias.getSemestre();
        status = monitorias.getStatus();
        dataCadastro = monitorias.getDataCadastro();
        ultimaAtualizacao = monitorias.getUltimaAtualizacao();
    }
}
