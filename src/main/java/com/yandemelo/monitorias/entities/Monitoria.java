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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "monitoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Monitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private User professorId;

    @OneToOne
    @JoinColumn(name = "monitor_id", nullable = false)
    private User monitorId;

    private String disciplina;
    private String curso;
    private String semestre;
    private String status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;
}
