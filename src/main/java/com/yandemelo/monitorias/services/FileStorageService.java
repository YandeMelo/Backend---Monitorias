package com.yandemelo.monitorias.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.exceptions.BadRequestException;
import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;
import com.yandemelo.monitorias.services.authServices.AuthorizationService;
import com.yandemelo.monitorias.utils.FileStorageProperties;

@Service
public class FileStorageService {
    
    @Autowired
    private AuthorizationService userService;

    @Autowired
    private UserRepository userRepository;

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    @Transactional
    public String inserirArquivo(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (!(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") || file.getContentType().equals("image/gif"))){
            throw new BadRequestException("Formato de arquivo inválido!");
        }

        try {
            if (Files.notExists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            User user = userService.authenticated();

            if (user.getFotoPerfil() != null) {
                Path currentImagePath = Paths.get(fileStorageLocation.toString(), Paths.get(user.getFotoPerfil()).getFileName().toString());
                Files.deleteIfExists(currentImagePath);
            }

            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/arquivos/buscar/")
                    .path(fileName)
                    .toUriString();

            user.setFotoPerfil(fileDownloadUri);
            userRepository.save(user);

            return fileDownloadUri;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Transactional
    public Resource buscarArquivo(String fileName) throws IOException {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        }

    }


}
