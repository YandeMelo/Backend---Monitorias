package com.yandemelo.monitorias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yandemelo.monitorias.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{
    

   @Query("SELECT a FROM Arquivo a " +
          "INNER JOIN CandidatoMonitoria cm ON a.id = cm.pdfHistoricoEscolar.id " +
          "WHERE a.idAluno = :idAluno")
   Arquivo getArquivoPorIdAluno(Long idAluno);

   @Query("SELECT a FROM Arquivo a WHERE a.id = :idArquivo AND a.idAluno = :idAluno")
   Arquivo getArquivo(Long idArquivo, Long idAluno);
}
