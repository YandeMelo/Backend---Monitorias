package com.yandemelo.monitorias.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.yandemelo.monitorias.utils.Email;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(Email email){
        var message = new SimpleMailMessage();
        message.setFrom("noreply-monitorias@poli.br");
        message.setTo(email.to()); 
        message.setSubject(email.subject()); 
        message.setText(email.body());
        mailSender.send(message);
    }
}
