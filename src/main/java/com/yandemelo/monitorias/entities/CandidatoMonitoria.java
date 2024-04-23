package com.yandemelo.monitorias.entities;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidato_monitoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CandidatoMonitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "aluno_id")
    private User alunoId;

    @OneToOne
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoriaId;

    @OneToOne
    @JoinColumn(name = "historico_escolar_id")
    private Arquivo pdfHistoricoEscolar;

    @NotNull
    private LocalDate dataSolicitacao;

    @NotNull
    private String statusCandidatura;

    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

}
