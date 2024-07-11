package com.yandemelo.monitorias.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
        .requestMatchers(HttpMethod.GET, "/monitorias/disponiveis").hasRole("ALUNO")
        .requestMatchers(HttpMethod.GET, "/monitorias/info/{idMonitoria}").hasRole("ALUNO")
        .requestMatchers(HttpMethod.POST, "/monitorias/abrir").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.POST, "/monitorias/suspender/{id}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.POST, "/monitorias/candidatar/{id}").hasRole("ALUNO")
        .requestMatchers(HttpMethod.GET, "/professor/avaliar/{idAluno}/{idMonitoria}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/recusar/{idAluno}/{idMonitoria}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/aprovar/{idAluno}/{idMonitoria}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/candidatos/{idMonitoria}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/monitorias").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/monitoria/avaliar/{idMonitoria}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/professor/historico/{idAluno}").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.GET, "/aluno/inscricao").hasRole("ALUNO")
        .requestMatchers(HttpMethod.GET, "/aluno/monitoria").hasRole("ALUNO")
        .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Abrange todas as rotas
                    .allowedOrigins("http://localhost:4200", "https://monitorias-two.vercel.app") // Permite a origem específica
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                    .allowedHeaders("*") // Permite todos os cabeçalhos
                    .exposedHeaders("Authorization", "Link", "X-Total-Count") // Expor os cabeçalhos necessários
                    .allowCredentials(true); // Permite o envio de cookies
            }
        };
    }
}