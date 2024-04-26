package com.yandemelo.monitorias.dto.candidaturaAluno;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoMonitoriaDTO {

    private Long alunoId;
    private Monitoria monitoriaId;
    private Long pdfHistoricoEscolar;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public CandidatoMonitoriaDTO(User user, Monitoria monitoria, Long historicoEscolarId) {
        alunoId = user.getId();
        monitoriaId = monitoria;
        pdfHistoricoEscolar = historicoEscolarId;
        dataSolicitacao = LocalDate.now();
        statusCandidatura = StatusCandidatura.AGUARDANDO_APROVACAO;
    }

}
