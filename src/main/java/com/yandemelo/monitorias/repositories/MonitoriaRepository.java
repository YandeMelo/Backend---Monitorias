package com.yandemelo.monitorias.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long>{
    

    @Query("""
           SELECT m FROM Monitoria m \
           JOIN m.professorId p \
           WHERE p.id = :professorId \
           AND m.disciplina = :disciplina \
           AND m.semestre = :semestre \
           AND m.curso = :curso\
           """)
    Monitoria verificarMonitoriaExistente(@Param("professorId") Long professorId, 
                                                     @Param("disciplina") String disciplina, 
                                                     @Param("semestre") String semestre, 
                                                     @Param("curso") CursosExistentes curso);

    @Query("SELECT m FROM Monitoria m WHERE m.curso = :curso AND m.status = DISPONIVEL")                                                 
    Page<Monitoria> consultarMonitoriasDisponiveis(CursosExistentes curso, Pageable pageable);

    @Query("SELECT m FROM Monitoria m WHERE m.curso = :curso AND m.id = :idMonitoria")                                                 
    Monitoria consultarInfoMonitoria(CursosExistentes curso, Long idMonitoria);

    @Query("SELECT m FROM Monitoria m WHERE m.monitorId.id = :candidato")   
    Monitoria buscarPorCandidato(Long candidato);
    
    @Query("SELECT m FROM Monitoria m WHERE m.professorId.id = :professor")   
    Page<Monitoria> buscarPorProfessor(Long professor, Pageable pageable);


}
