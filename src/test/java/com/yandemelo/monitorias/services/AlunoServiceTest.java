package com.yandemelo.monitorias.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

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
    @DisplayName("Status da monitoria que o aluno foi aceito")
    void testStatusMonitoriaAceito() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, aluno, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.ANDAMENTO, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoria, arquivo, LocalDate.now(), StatusCandidatura.APROVADO, monitoria.getDataCadastro(), monitoria.getUltimaAtualizacao());
        
        when(monitoriaRepository.buscarPorCandidato(aluno.getId())).thenReturn(monitoria);

        assertEquals(monitoria.getMonitorId(), aluno);
        assertEquals(candidatoMonitoria.getMonitoriaId(), monitoria);
        assertEquals(candidatoMonitoria.getAlunoId(), aluno);
        assertEquals(candidatoMonitoria.getStatusCandidatura(), StatusCandidatura.APROVADO);
        assertNotEquals(monitoria.getStatus(), StatusMonitoria.DISPONIVEL);
    }

    @Test
    @DisplayName("Status da monitoria que o aluno ainda não foi aceito")
    void testStatusMonitoriaNaoAceito() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoria, arquivo, LocalDate.now(), StatusCandidatura.RECUSADO, monitoria.getDataCadastro(), monitoria.getUltimaAtualizacao());
        
        when(monitoriaRepository.buscarPorCandidato(aluno.getId())).thenReturn(null);

        assertNotEquals(monitoria.getMonitorId(), aluno);
        assertNotEquals(candidatoMonitoria.getStatusCandidatura(), StatusCandidatura.APROVADO);
        assertEquals(candidatoMonitoria.getAlunoId(), aluno);
        assertEquals(candidatoMonitoria.getMonitoriaId(), monitoria);
        assertEquals(monitoria.getStatus(), StatusMonitoria.DISPONIVEL);
    }

    @Test
    @DisplayName("Candidatar aluno em uma monitoria")
    void testCandidatarAluno() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoriaAberta = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        Optional<Monitoria> monitoria = this.monitoriaRepository.findById(1L);
        when(monitoriaRepository.findById(1L)).thenReturn(monitoria);
        arquivoRepository.save(arquivo);
        
        when( candidatoMonitoriaRepository.verInscricao(aluno)).thenReturn(null);
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoriaAberta, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoriaAberta.getDataCadastro(), monitoriaAberta.getUltimaAtualizacao());
 
        assertEquals(candidatoMonitoria.getStatusCandidatura(), StatusCandidatura.AGUARDANDO_APROVACAO);
        assertEquals(candidatoMonitoria.getAlunoId(), aluno);
        assertEquals(candidatoMonitoria.getMonitoriaId(), monitoriaAberta);
        assertEquals(aluno.getCurso(), monitoriaAberta.getCurso());
        assertEquals(monitoriaAberta.getStatus(), StatusMonitoria.DISPONIVEL);
    }

    @Test
    @DisplayName("Aluno já candidatado me outra monitoria")
    void testCandidatarAlunoJaCandidatado() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoriaAberta = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", null, CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(userService.authenticated()).thenReturn(aluno);
        Optional<Monitoria> monitoria = this.monitoriaRepository.findById(1L);
        when(monitoriaRepository.findById(1L)).thenReturn(monitoria);
        arquivoRepository.save(arquivo);
        
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoriaAberta, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoriaAberta.getDataCadastro(), monitoriaAberta.getUltimaAtualizacao());
        when( candidatoMonitoriaRepository.verInscricao(aluno)).thenReturn(candidatoMonitoria);
 
        assertEquals(candidatoMonitoria.getStatusCandidatura(), StatusCandidatura.AGUARDANDO_APROVACAO);
        assertEquals(candidatoMonitoria.getAlunoId(), aluno);
        assertEquals(candidatoMonitoria.getMonitoriaId(), monitoriaAberta);
        assertEquals(aluno.getCurso(), monitoriaAberta.getCurso());
        assertEquals(monitoriaAberta.getStatus(), StatusMonitoria.DISPONIVEL);
    }

}
