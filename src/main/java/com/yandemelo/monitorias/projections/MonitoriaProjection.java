package com.yandemelo.monitorias.projections;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.User;

public interface MonitoriaProjection {
    Long getId();
    String getDisciplina();
    String getSemestre();
    String getCurso();
    User getProfessorId();
    User getMonitorId();
    String getStatus();
    LocalDate getDataCadastro();
    LocalDate getUltimaAtualizacao();

}
