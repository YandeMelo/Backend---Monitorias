package com.yandemelo.monitorias.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yandemelo.monitorias.dto.codigoVerificacao.AlterarSenhaDTO;
import com.yandemelo.monitorias.dto.codigoVerificacao.VerificarCodigoDTO;
import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;
import com.yandemelo.monitorias.utils.Email;
import com.yandemelo.monitorias.exceptions.BadRequestException;

@Service
public class RecuperarSenhaService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public EmailService emailService;

    @Transactional
    public ResponseEntity<String> solicitarCodigo(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new BadRequestException("Email inválido!");
        }
        user.setCodigoRecuperacaoSenha(gerarCodigo(user.getId()));
        user.setDataEnvioCodigo(new Date());
        userRepository.saveAndFlush(user);

        Email emailRecuperacao = new Email(user.getEmail());

        emailService.sendEmail(emailRecuperacao);
        return ResponseEntity.ok().body("Código Enviado!");
    }

    @Transactional
    public ResponseEntity<Boolean> verificarCodigo(VerificarCodigoDTO user){
        User userBanco = userRepository.findByEmailAndCodigoRecuperacaoSenha(user.getEmail(), user.getCodigoRecuperacaoSenha());
        if (userBanco == null) {
            throw new BadRequestException("Email ou Código inválido!");
        }
        return ResponseEntity.ok().body(true);
    }

    @Transactional
    public ResponseEntity<String> alterarSenha(AlterarSenhaDTO user){
        User userBanco = userRepository.findByEmailAndCodigoRecuperacaoSenha(user.getEmail(), user.getCodigoRecuperacaoSenha());
        if (userBanco == null) {
            throw new BadRequestException("Email ou Código inválido!");
        }
        Date diferenca = new Date(new Date().getTime() - userBanco.getDataEnvioCodigo().getTime());
        if (diferenca.getTime()/1000 < 900) {
            String senhaCriptografada = new BCryptPasswordEncoder().encode(user.getPassword());
            userBanco.setPassword(senhaCriptografada);
            userBanco.setCodigoRecuperacaoSenha(null);
            userBanco.setDataEnvioCodigo(null);
            userRepository.saveAndFlush(userBanco);
            return ResponseEntity.ok().body("Senha alterada com sucesso!");
        } else {
            throw new BadRequestException("Tempo expirado, solicite um novo código.");
        }
    }

    private String gerarCodigo(Long id) {
        DateFormat format = new SimpleDateFormat("ddMMyyyyHHmmssmm");
        return format.format(new Date()) + id;
    }

}
