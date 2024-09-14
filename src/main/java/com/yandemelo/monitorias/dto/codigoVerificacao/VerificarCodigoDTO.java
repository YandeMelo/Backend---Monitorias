package com.yandemelo.monitorias.dto.codigoVerificacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificarCodigoDTO {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String codigoRecuperacaoSenha;
}