package com.yandemelo.monitorias.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;
import com.yandemelo.monitorias.utils.Email;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(Email email) {
       try {
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreply@gmail.com");
        helper.setSubject("Recuperação de Senha");
        helper.setTo(email.to());
        
        String template  = carregaTemplateEmail();

        template = template.replace("#{nome}", userRepository.findUserByEmail(email.to()).getNome());
        template = template.replace("#{codigo}", userRepository.findUserByEmail(email.to()).getCodigoRecuperacaoSenha());
        helper.setText(template, true);
           mailSender.send(message);
       } catch (Exception exception) {
           System.out.println("Falha ao enviar o email");
       }
    }

    public String carregaTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/template-email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
