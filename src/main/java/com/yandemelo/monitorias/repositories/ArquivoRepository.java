package com.yandemelo.monitorias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yandemelo.monitorias.entities.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long>{
    

   @Query("SELECT a FROM Arquivo a WHERE a.idAluno = :idAluno")
   Arquivo getArquivoPorIdAluno(Long idAluno);

}
