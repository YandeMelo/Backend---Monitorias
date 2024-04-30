package com.yandemelo.monitorias.dto.authDTO;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.UserRole;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;

public record RegisterDTO(String nome, String cpf, CursosExistentes curso, String email, String password, String sub, String fotoPerfil, Boolean ativo, 
LocalDate dataDesativacao, UserRole tipoUsuario, LocalDate dataCadastro, LocalDate ultimaAtualizacao) {
    
}
