package com.yandemelo.monitorias.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private User professorId;
    
    @OneToOne
    @JoinColumn(name = "monitor_id", nullable = true)
    private User monitorId;
    
    private String disciplina;
    
    @OneToMany(mappedBy = "monitoriaId")
    private Set<CandidatoMonitoria> candidatos = new HashSet<CandidatoMonitoria>();
    
    @Enumerated(EnumType.STRING)
    private CursosExistentes curso;
    private String semestre;
    
    @Enumerated(EnumType.STRING)
    private StatusMonitoria status;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;
}
