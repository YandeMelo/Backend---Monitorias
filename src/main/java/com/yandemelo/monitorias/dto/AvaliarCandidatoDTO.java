package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliarCandidatoDTO {

    private Long id;
    private String nome;
    private String email;
    private CursosExistentes curso;
    private String fotoPerfil;
    private Long idHistoricoEscolar;
    private LocalDate dataSolicitacao;
    private StatusCandidatura statusCandidatura;
    
    public AvaliarCandidatoDTO(User aluno, CandidatoMonitoria monitoria) {
        id = aluno.getId();
        nome = aluno.getNome();
        email = aluno.getEmail();
        curso = aluno.getCurso();
        fotoPerfil = aluno.getFotoPerfil();
        idHistoricoEscolar = monitoria.getPdfHistoricoEscolar().getId();
        dataSolicitacao = monitoria.getDataSolicitacao();
        statusCandidatura = monitoria.getStatusCandidatura();
    }

}
