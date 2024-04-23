package com.yandemelo.monitorias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.projections.MonitoriaProjection;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long>{
    

    @Query(value = "SELECT * FROM monitoria m " +
                   "WHERE m.professor_id = :professorId " +
                   "AND m.disciplina = :disciplina " +
                   "AND m.semestre = :semestre " +
                   "AND m.curso = :curso",
           nativeQuery = true)
    MonitoriaProjection verificarMonitoriaExistente(@Param("professorId") Long professorId, 
                                                     @Param("disciplina") String disciplina, 
                                                     @Param("semestre") String semestre, 
                                                     @Param("curso") String curso);




}
