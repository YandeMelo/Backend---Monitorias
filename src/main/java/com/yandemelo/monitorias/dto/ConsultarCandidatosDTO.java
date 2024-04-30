package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultarCandidatosDTO {
    
    private Long id;
    private String nome;
    private String email;
    private CursosExistentes curso;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;

    public ConsultarCandidatosDTO(CandidatoMonitoria candidato) {
        id = candidato.getAlunoId().getId();
        nome = candidato.getAlunoId().getNome();
        email = candidato.getAlunoId().getEmail();
        curso = candidato.getAlunoId().getCurso();
        dataSolicitacao = candidato.getDataSolicitacao();
        statusCandidatura = candidato.getStatusCandidatura();
    }
}
