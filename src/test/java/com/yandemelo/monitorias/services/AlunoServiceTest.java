package com.yandemelo.monitorias.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.CandidatoMonitoria;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.authEntities.UserRole;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;
import com.yandemelo.monitorias.entities.enums.StatusCandidatura;
import com.yandemelo.monitorias.entities.enums.StatusMonitoria;
import com.yandemelo.monitorias.repositories.ArquivoRepository;
import com.yandemelo.monitorias.repositories.CandidatoMonitoriaRepository;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@DataJpaTest
@ActiveProfiles("test")
public class AlunoServiceTest {

    @Mock
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;
    @Mock
    private MonitoriaRepository monitoriaRepository;
    @Mock
    private ArquivoRepository arquivoRepository;
    @Mock
    private AuthorizationService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Status da candidatura caso esteja inscrito em uma Monitoria")
    void testStatusCandidaturaInscrito() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoria, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoria.getDataCadastro(), monitoria.getUltimaAtualizacao());
 
        when(candidatoMonitoriaRepository.verInscricao(aluno)).thenReturn(candidatoMonitoria);

        assertThat(candidatoMonitoria.getPdfHistoricoEscolar()).isEqualTo(arquivo);
        assertThat(candidatoMonitoria.getMonitoriaId()).isEqualTo(monitoria);
        assertThat(candidatoMonitoria.getAlunoId()).isEqualTo(aluno);
    }

    @Test
    @DisplayName("Status da candidatura caso o aluno não esteja inscrito")
    void testStatusCandidaturaNaoInscrito(){    
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        when(candidatoMonitoriaRepository.verInscricao(aluno)).thenReturn(null);

        assertNull(monitoria.getCandidatos());
        assertThat(candidatoMonitoriaRepository.verInscricao(aluno)).isNull();
    }

    @Test
    void testCandidatarAluno() {

    }

    @Test
    void testSalvarArquivo() {

    }

    @Test
    void testSalvarCandidato() {

    }


    @Test
    void testStatusMonitoria() {

    }
}
