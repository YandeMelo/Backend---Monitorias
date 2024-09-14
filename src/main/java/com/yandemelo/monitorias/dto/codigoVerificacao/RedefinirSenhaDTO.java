package com.yandemelo.monitorias.dto.codigoVerificacao;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RedefinirSenhaDTO {
    
    @NotBlank
    private String senhaAtual;
    
    @NotBlank
    private String novaSenha;

}
