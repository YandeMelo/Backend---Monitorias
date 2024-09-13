package com.yandemelo.monitorias.entities.authEntities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yandemelo.monitorias.entities.Monitoria;
import com.yandemelo.monitorias.entities.enums.CursosExistentes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Enumerated(EnumType.STRING)
    private CursosExistentes curso;

    @NotBlank
    @Column(unique = true)
    private String cpf;

    @NotBlank
    @Column(unique = true)
    @Email
    private String email;

    private String codigoRecuperacaoSenha;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvioCodigo;

    @NotBlank
    private String password;
    private String fotoPerfil;
    private Boolean ativo;
    private LocalDate dataDesativacao;

    @NotNull
    private UserRole tipoUsuario;
    private LocalDate dataCadastro;
    private LocalDate ultimaAtualizacao;

    @OneToMany(mappedBy = "professorId", fetch = FetchType.EAGER)
    private List<Monitoria> monitorias = new ArrayList<>();
    
    public User(String nome,  String cpf, CursosExistentes curso, String email, String fotoPerfil, Boolean ativo,
            LocalDate dataDesativacao, UserRole tipoUsuario, LocalDate dataCadastro, LocalDate ultimaAtualizacao,
            String encryptedPassword) {
                this.nome = nome;
                this.cpf = cpf;
                this.curso = curso;
                this.email = email;
                this.fotoPerfil = fotoPerfil;
                this.ativo = ativo;
                this.dataDesativacao = dataDesativacao;
                this.tipoUsuario = tipoUsuario;
                this.dataCadastro = dataCadastro;
                this.ultimaAtualizacao = ultimaAtualizacao;
                this.password = encryptedPassword;
    }

    public boolean hasRole(String role) {
        Collection<? extends GrantedAuthority> authorities = this.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipoUsuario == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_PROFESSOR"), new SimpleGrantedAuthority("ROLE_ALUNO"));
        }else if (this.tipoUsuario == UserRole.PROFESSOR){
            return List.of(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
        }
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}

