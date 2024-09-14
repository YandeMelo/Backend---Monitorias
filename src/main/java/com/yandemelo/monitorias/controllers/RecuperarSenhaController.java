package com.yandemelo.monitorias.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yandemelo.monitorias.dto.codigoVerificacao.AlterarSenhaDTO;
import com.yandemelo.monitorias.dto.codigoVerificacao.RedefinirSenhaDTO;
import com.yandemelo.monitorias.dto.codigoVerificacao.VerificarCodigoDTO;
import com.yandemelo.monitorias.services.RecuperarSenhaService;

@RestController
@RequestMapping("/recuperar")
public class RecuperarSenhaController {
    
    @Autowired
    private RecuperarSenhaService recuperarSenhaService;

    @PostMapping("/codigo")
    public ResponseEntity<String> recuperarCodigo(@RequestParam String email){
        return recuperarSenhaService.solicitarCodigo(email);
    }

    @PostMapping("/verificar")
    public ResponseEntity<Boolean> verificarCodigo(@RequestBody VerificarCodigoDTO user){
        return recuperarSenhaService.verificarCodigo(user);
    }  

    @PostMapping("/alterarSenha")
    public ResponseEntity<String> alterarSenha(@RequestBody AlterarSenhaDTO user){
        return recuperarSenhaService.alterarSenha(user);
    }

    @PostMapping("/redefinirSenha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaDTO dto){
        return recuperarSenhaService.redefinirSenha(dto);
    }

}
