package com.yandemelo.monitorias.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;

public interface CandidatoMonitoriaRepository extends JpaRepository<CandidatoMonitoria, Long>{
    
    @Query("SELECT c FROM CandidatoMonitoria c WHERE c.alunoId = :idCandidato " )
    CandidatoMonitoria verInscricao (User idCandidato);
    
    @Query("SELECT c FROM CandidatoMonitoria c WHERE c.alunoId = :candidato AND c.monitoriaId = :monitoria " )
    CandidatoMonitoria verInscricaoMonitoria(User candidato, Monitoria monitoria);
    
    @Query("SELECT cm " +
        "FROM CandidatoMonitoria cm " +
        "INNER JOIN cm.alunoId u " +
        "INNER JOIN cm.monitoriaId m " +
        "WHERE m.id = :idMonitoria")
    CandidatoMonitoria consultarAlunoEMonitoria(Long idMonitoria);

    @Query("SELECT cm " +
        "FROM CandidatoMonitoria cm " +
        "INNER JOIN cm.alunoId u " +
        "INNER JOIN cm.monitoriaId m " +
        "WHERE m.monitorId.id = :idMonitor")
    CandidatoMonitoria consultarAlunoCandidatado(Long idMonitor);

    @Query("SELECT cm " +
        "FROM CandidatoMonitoria cm " +
        "INNER JOIN cm.alunoId u " +
        "INNER JOIN cm.monitoriaId m " +
        "INNER JOIN cm.pdfHistoricoEscolar a " +
        "WHERE m.id = :idMonitoria")
    Page<CandidatoMonitoria> consultarCandidatos(Long idMonitoria, Pageable pageable);

    @Query("SELECT cm FROM CandidatoMonitoria cm " +
            "INNER JOIN cm.monitoriaId m " + 
            "WHERE m.id = :idMonitoria ")
    List<CandidatoMonitoria> consultarCandidaturas(Long idMonitoria);
}
