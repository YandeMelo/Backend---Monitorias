package com.yandemelo.monitorias.dto;

import java.time.LocalDate;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.entities.authEntities.UserRole;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String nome;
    private CursosExistentes curso;
    private String cpf;
    private String email;
    private String fotoPerfil;
    private Boolean ativo;
    private LocalDate dataDesativacao;
    private UserRole tipoUsuario;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

    public UserDTO(User user){
        id = user.getId();
        nome = user.getNome();
        curso = user.getCurso();
        cpf = user.getCpf();
        email = user.getEmail();
        fotoPerfil = user.getFotoPerfil();
        ativo = user.getAtivo();
        dataDesativacao = user.getDataDesativacao();
        tipoUsuario = user.getTipoUsuario();
        dataCadastro = user.getDataCadastro();
        ultimaAtualizacao = user.getUltimaAtualizacao();
    }

}
