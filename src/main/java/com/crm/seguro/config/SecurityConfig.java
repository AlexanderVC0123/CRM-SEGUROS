package com.crm.seguro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desactiva protección CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()  // Permite todas las rutas sin autenticación
            );

        return http.build();
    }
}
