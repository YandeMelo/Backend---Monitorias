package com.yandemelo.monitorias.dto;

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
    private Long monitoriaId;
    private Long pdfHistoricoEscolar;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public CandidatoMonitoriaDTO(User user, Monitoria monitoria, Long historicoEscolarId) {
        alunoId = user.getId();
        monitoriaId = monitoria.getId();
        pdfHistoricoEscolar = historicoEscolarId;
        dataSolicitacao = LocalDate.now();
        statusCandidatura = StatusCandidatura.AGUARDANDO_APROVACAO;
    }

}
