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
public class BuscarStatusCandidatura {


    private StatusMonitoriaDTO monitoriaId;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public BuscarStatusCandidatura(CandidatoMonitoria candidato, StatusMonitoriaDTO monitoria) {
        monitoriaId = monitoria;
        dataSolicitacao = candidato.getDataSolicitacao();
        statusCandidatura = candidato.getStatusCandidatura();
    }
    

}
