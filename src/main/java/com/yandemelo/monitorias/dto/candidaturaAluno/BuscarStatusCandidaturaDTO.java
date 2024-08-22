package com.yandemelo.monitorias.dto.candidaturaAluno;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuscarStatusCandidaturaDTO {

    private StatusMonitoriaDTO monitoriaId;
    private Long idRelatorio;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public BuscarStatusCandidaturaDTO(CandidatoMonitoria candidato, StatusMonitoriaDTO monitoria) {
        monitoriaId = monitoria;
        if (candidato.getRelatorioMonitoria() != null) {
            idRelatorio = candidato.getRelatorioMonitoria().getId();
        } else {
            idRelatorio = null;
        }
        dataSolicitacao = candidato.getDataSolicitacao();
        statusCandidatura = candidato.getStatusCandidatura();
    }
    

}
