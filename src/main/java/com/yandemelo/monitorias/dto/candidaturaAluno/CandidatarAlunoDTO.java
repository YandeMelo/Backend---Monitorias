package com.yandemelo.monitorias.dto.candidaturaAluno;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatarAlunoDTO {

    private String nomeAluno;
    private StatusMonitoriaDTO monitoria;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public CandidatarAlunoDTO(User user, StatusMonitoriaDTO inscricao) {
        nomeAluno = user.getNome();
        monitoria = inscricao;
        dataSolicitacao = LocalDate.now();
        statusCandidatura = StatusCandidatura.AGUARDANDO_APROVACAO;
    }

}
