package com.yandemelo.monitorias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.authEntities.User;

public interface CandidatoMonitoriaRepository extends JpaRepository<CandidatoMonitoria, Long>{
    
    @Query("SELECT c FROM CandidatoMonitoria c WHERE c.alunoId = :idCandidato")
    CandidatoMonitoria verInscricao (User idCandidato);

}
