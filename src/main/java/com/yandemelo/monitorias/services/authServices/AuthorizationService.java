package com.yandemelo.monitorias.services.authServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yandemelo.monitorias.entities.authEntities.User;
import com.yandemelo.monitorias.repositories.authRepositories.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {
    
    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

    public User authenticated(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return repository.findUserByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
