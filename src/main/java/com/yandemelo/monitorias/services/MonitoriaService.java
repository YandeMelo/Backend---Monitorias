package com.yandemelo.monitorias.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.ConsultarMonitoriasDTO;
import com.yandemelo.monitorias.entities.Arquivo;
import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.exceptions.BadRequestException;
import com.yandemelo.monitorias.exceptions.ResourceNotFoundException;
import com.yandemelo.monitorias.repositories.ArquivoRepository;
import com.yandemelo.monitorias.repositories.MonitoriaRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;

@Service
public class MonitoriaService {
    
    @Autowired
    private MonitoriaRepository monitoriaRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private AuthorizationService userService;
    
    @Transactional(readOnly = true)
    public Page<ConsultarMonitoriasDTO> consultarMonitoriasDisponiveis (Pageable pageable){
        User user = userService.authenticated();
        Page<Monitoria> monitorias = monitoriaRepository.consultarMonitoriasDisponiveis(user.getCurso(), pageable);
        return monitorias.map(x -> new ConsultarMonitoriasDTO(x));
    }

    @Transactional(readOnly = true)
    public ConsultarMonitoriasDTO consultarInfoMonitoria(Long idMonitoria) {
        User user = userService.authenticated();
        Monitoria monitoria = monitoriaRepository.consultarInfoMonitoria(user.getCurso(), idMonitoria);
        if (monitoria == null) {
            throw new BadRequestException("Esta monitoria não pertence ao seu curso.");
        }
        return new ConsultarMonitoriasDTO(monitoria);
    }

    @Transactional
    public ResponseEntity<ByteArrayResource> baixarArquivo(Long idArquivo, Long idAluno){
        try {
            Arquivo arquivo = arquivoRepository.getArquivo(idArquivo, idAluno);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getNomeArquivo());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(arquivo.getConteudo().length));
            return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(arquivo.getConteudo()));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Arquivo não encontrado.");
        }
    }
}
