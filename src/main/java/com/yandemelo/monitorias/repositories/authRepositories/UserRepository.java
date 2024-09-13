package com.yandemelo.monitorias.repositories.authRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.yandemelo.monitorias.entities.authEntities.User;


public interface UserRepository extends JpaRepository <User, Long>{
        
    UserDetails findByEmail(String email);
    
    User findUserByEmail(String email);
        
    User findByEmailAndCodigoRecuperacaoSenha(String email, String codigoRecuperacaoSenha);
}
