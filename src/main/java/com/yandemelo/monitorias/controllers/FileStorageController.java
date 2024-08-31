package com.yandemelo.monitorias.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yandemelo.monitorias.services.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("api/arquivos")
public class FileStorageController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/inserir")
    public ResponseEntity<String> inserirArquivo(@RequestParam("file") MultipartFile file) throws URISyntaxException {

        String uri = fileStorageService.inserirArquivo(file);
        return ResponseEntity.created(new URI(uri)).body(uri);
    }

    @GetMapping("/buscar/{fileName:.+}")
    public ResponseEntity<Resource> buscarArquivo(@PathVariable String fileName, HttpServletRequest request)
            throws IOException {

        Resource resource = fileStorageService.buscarArquivo(fileName);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

}
