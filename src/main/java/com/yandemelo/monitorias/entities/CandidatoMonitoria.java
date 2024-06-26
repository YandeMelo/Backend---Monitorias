package com.yandemelo.monitorias.entities;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "monitoria_id")
    private Monitoria monitoriaId;

    @OneToOne
    @JoinColumn(name = "historico_escolar_id")
    private Arquivo pdfHistoricoEscolar;

    @NotNull
    private LocalDate dataSolicitacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusCandidatura statusCandidatura;

    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

}
