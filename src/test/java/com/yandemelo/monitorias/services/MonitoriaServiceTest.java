package com.yandemelo.monitorias.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.authEntities.UserRole;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@DataJpaTest
@ActiveProfiles("test")
public class MonitoriaServiceTest {

    @Mock
    private MonitoriaRepository monitoriaRepository;
    
    @Mock
    private AuthorizationService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Consultar monitorias")
    void testConsultarMonitoriasDisponiveis() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());

        when(userService.authenticated()).thenReturn(aluno);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Monitoria> pageWithMonitoria = new PageImpl<>(Collections.singletonList(monitoria), pageable, 1);
        when(monitoriaRepository.consultarMonitoriasDisponiveis(aluno.getCurso(), pageable)).thenReturn(pageWithMonitoria);

        Page<Monitoria> monitorias = monitoriaRepository.consultarMonitoriasDisponiveis(aluno.getCurso(), pageable);
        
        assertNotNull(monitorias); 
        assertTrue(monitorias.hasContent());
        assertTrue(monitorias.getContent().contains(monitoria));

    }
}
