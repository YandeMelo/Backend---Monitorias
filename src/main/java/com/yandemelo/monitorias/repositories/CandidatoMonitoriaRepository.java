package com.yandemelo.monitorias.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.authEntities.User;

public interface CandidatoMonitoriaRepository extends JpaRepository<CandidatoMonitoria, Long>{
    
    @Query("SELECT c FROM CandidatoMonitoria c WHERE c.alunoId = :idCandidato " )
    CandidatoMonitoria verInscricao (User idCandidato);

    @Query("SELECT cm " +
        "FROM CandidatoMonitoria cm " +
        "INNER JOIN cm.alunoId u " +
        "INNER JOIN cm.monitoriaId m " +
        "INNER JOIN cm.pdfHistoricoEscolar a " +
        "WHERE m.id = :idMonitoria")
    List<CandidatoMonitoria> consultarCandidatos(Long idMonitoria);


}
