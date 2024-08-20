package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliarMonitoriaDTO {

    private Long monitoriaId;
    private String disciplina;
    private CursosExistentes curso;
    private String professorNome;
    private String semestre;
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

    private String nome;
    private String email;
    private String fotoPerfil;
    private Long idHistoricoEscolar;

    public AvaliarMonitoriaDTO(CandidatoMonitoria monitoria) {
        monitoriaId = monitoria.getMonitoriaId().getId();
        disciplina = monitoria.getMonitoriaId().getDisciplina();
        curso = monitoria.getMonitoriaId().getCurso();
        professorNome = monitoria.getMonitoriaId().getProfessorId().getNome();
        semestre = monitoria.getMonitoriaId().getSemestre();
        status = monitoria.getMonitoriaId().getStatus();
        dataCadastro = monitoria.getDataCadastro();
        ultimaAtualizacao = monitoria.getUltimaAtualizacao();

        nome = monitoria.getAlunoId().getNome();
        email = monitoria.getAlunoId().getEmail();
        fotoPerfil = monitoria.getAlunoId().getFotoPerfil();
        idHistoricoEscolar = monitoria.getPdfHistoricoEscolar().getId();
    }

}
