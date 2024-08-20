package com.yandemelo.monitorias.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

public class ProfessorServiceTest {

    @Mock
    private AuthorizationService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ArquivoRepository arquivoRepository;
    @Mock
    private MonitoriaRepository monitoriaRepository;
    @Mock
    private CandidatoMonitoriaRepository candidatoMonitoriaRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Listar monitorias abertas pelo professor")
    void testMinhasMonitorias() {
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria1 = new Monitoria(1L, professor, null, "MATEMÁTICA_DISCRETA", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        Monitoria monitoria2 = new Monitoria(2L, professor, null, "TEORIA_DA_COMPUTACAO", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        Monitoria monitoria3 = new Monitoria(3L, professor, null, "BANCO_DE_DADOS", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Monitoria> mockedPage = new PageImpl<>(List.of(monitoria1, monitoria2, monitoria3), pageable, 3);

        when(monitoriaRepository.buscarPorProfessor(professor.getId(), pageable)).thenReturn(mockedPage);

        Page<Monitoria> monitorias = monitoriaRepository.buscarPorProfessor(professor.getId(), pageable);

        assertThat(monitorias.getContent()).contains(monitoria1, monitoria2, monitoria3);
    }

    @Test
    @DisplayName("Listar vazio no caso de não ter aberto monitoria")
    void testMinhasMonitoriasNull() {
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Monitoria> monitorias = monitoriaRepository.buscarPorProfessor(professor.getId(), pageable);

        assertThat(monitorias);
    }

    @Test
    @DisplayName("Ofertar nova monitoria")
    void testOfertarMonitoria() {
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        
        when(userService.authenticated()).thenReturn(professor);
        when(monitoriaRepository.verificarMonitoriaExistente(professor.getId(), "MATEMATICA_DISCRETA", "2020.2", professor.getCurso())).thenReturn(null);

        Monitoria monitoria = new Monitoria();
        monitoria.setCurso(professor.getCurso());
        monitoria.setDataCadastro(LocalDate.now());
        monitoria.setDisciplina("MATEMATICA_DISCRETA");
        monitoria.setSemestre("2020.2");
        monitoria.setStatus(StatusMonitoria.DISPONIVEL);
        monitoria.setUltimaAtualizacao(LocalDate.now());
        monitoria.setMonitorId(null);
        monitoria.setProfessorId(professor);
        monitoriaRepository.save(monitoria);
        
        assertEquals(professor, monitoria.getProfessorId());
        assertEquals(professor.getCurso(), monitoria.getCurso());
        assertEquals(StatusMonitoria.DISPONIVEL, monitoria.getStatus());
        assertNotNull(monitoria.getDataCadastro());
        assertNotNull(monitoria.getDisciplina());
        assertNotNull(monitoria.getSemestre());
        assertNotNull(monitoria.getUltimaAtualizacao());

    }

    @Test
    @DisplayName("Ofertar monitoria que já foi ofertada")
    void testOfertarMonitoriaJaOfertada() {
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMATICA_DISCRETA", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
    
        when(userService.authenticated()).thenReturn(professor);
        when(monitoriaRepository.verificarMonitoriaExistente(professor.getId(), "MATEMATICA_DISCRETA", "2024.2", professor.getCurso())).thenReturn(monitoria);

        assertEquals(monitoriaRepository.verificarMonitoriaExistente(professor.getId(), "MATEMATICA_DISCRETA", "2024.2", professor.getCurso()), monitoria);

    }

    @Test
    @DisplayName("Verificar candidatos inscritos na monitoria")
    void testConsultarCandidatos() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoriaAberta = new Monitoria(1L, professor, null, "MATEMATICA_DISCRETA", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        Optional<Monitoria> monitoria = this.monitoriaRepository.findById(1L);
        when(monitoriaRepository.findById(monitoriaAberta.getId())).thenReturn(monitoria);

        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoriaAberta, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoriaAberta.getDataCadastro(), monitoriaAberta.getUltimaAtualizacao());
        when(candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoriaAberta)).thenReturn(candidatoMonitoria);
        
        assertEquals(monitoriaAberta, candidatoMonitoria.getMonitoriaId());
        assertEquals(aluno, candidatoMonitoria.getAlunoId());
    }

    @Test
    void testAvaliarCandidato() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMATICA_DISCRETA", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        Optional<User> alunoOptional = this.userRepository.findById(aluno.getId());
        when(userRepository.findById(aluno.getId())).thenReturn(alunoOptional);
        Optional<Monitoria> monitoriaOptional = monitoriaRepository.findById(monitoria.getId());
        when(monitoriaRepository.findById(monitoria.getId())).thenReturn(monitoriaOptional);
        
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoria, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoria.getDataCadastro(), monitoria.getUltimaAtualizacao());
        when(candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoria)).thenReturn(candidatoMonitoria);

        assertNotNull(candidatoMonitoria);
        assertEquals(monitoria, candidatoMonitoria.getMonitoriaId());
        assertEquals(aluno, candidatoMonitoria.getAlunoId());

    }

    @Test
    void testAprovarOuRecusarCandidatura() {
        User aluno = new User("Yan Melo", "123456789-00", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "yanmelo@gmail.com", "fotoPerfil.com", true, null, UserRole.ALUNO, LocalDate.now(), LocalDate.now(), "123456789");
        User professor = new User("Monteiro", "123456789-01", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "monteiro@gmail.com", "fotoPerfil.com", true, null, UserRole.PROFESSOR, LocalDate.now(), LocalDate.now(), "123456789");
        Monitoria monitoria = new Monitoria(1L, professor, null, "MATEMATICA_DISCRETA", CursosExistentes.ENGENHARIA_DA_COMPUTACAO, "2024.2", StatusMonitoria.DISPONIVEL, LocalDate.now(), LocalDate.now());
        
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, aluno.getId(), "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        Optional<User> alunoOptional = this.userRepository.findById(aluno.getId());
        when(userRepository.findById(aluno.getId())).thenReturn(alunoOptional);
        Optional<Monitoria> monitoriaOptional = monitoriaRepository.findById(monitoria.getId());
        when(monitoriaRepository.findById(monitoria.getId())).thenReturn(monitoriaOptional);
        
        CandidatoMonitoria candidatoMonitoria = new CandidatoMonitoria(1L, aluno, monitoria, arquivo, LocalDate.now(), StatusCandidatura.AGUARDANDO_APROVACAO, monitoria.getDataCadastro(), monitoria.getUltimaAtualizacao());
        when(candidatoMonitoriaRepository.verInscricaoMonitoria(aluno, monitoria)).thenReturn(candidatoMonitoria);

        monitoria.setStatus(StatusMonitoria.ANDAMENTO);
        monitoria.setMonitorId(aluno);
        monitoria.setUltimaAtualizacao(LocalDate.now());
        monitoriaRepository.save(monitoria);
        candidatoMonitoria.setStatusCandidatura(StatusCandidatura.APROVADO);
        candidatoMonitoria.setUltimaAtualizacao(LocalDate.now());
        candidatoMonitoriaRepository.save(candidatoMonitoria);

        assertNotNull(monitoria);
        assertEquals(aluno, monitoria.getMonitorId());
        assertEquals(StatusMonitoria.ANDAMENTO, monitoria.getStatus());

    }

    @Test
    void testBaixarHistoricoEscolar() {
        byte[] dadosArquivo = "Dados simulados do arquivo".getBytes();
        Arquivo arquivo = new Arquivo(1L, 1L, "Histórico Escolar Aluno", dadosArquivo, LocalDate.now(), LocalDate.now());
        
        when(arquivoRepository.getArquivo(1L, 1L)).thenReturn(arquivo);

        assertNotNull(arquivo);
        assertNotNull(arquivo.getNomeArquivo());
        assertNotNull(arquivo.getConteudo());

    }

    

}
