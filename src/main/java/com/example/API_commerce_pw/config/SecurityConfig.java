package com.example.API_commerce_pw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desativa a proteção CSRF. Se não desativar, o Spring bloqueia qualquer POST do Postman/React
            .csrf(AbstractHttpConfigurer::disable) 
            
            // Configura as permissões das rotas
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permite TODAS as requisições (GET, POST, etc) sem precisar de login
            );
            
        return http.build();
    }
}