package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.services.EmailService;
import com.yandemelo.monitorias.utils.Email;

@RestController
@RequestMapping("/email")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @PostMapping
    public void sendEmail(@RequestBody Email email) {
        emailService.sendEmail(email);
    }
}
