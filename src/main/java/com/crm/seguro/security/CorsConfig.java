package com.crm.seguro.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration config =  new CorsConfiguration();

        //Permitimos el frontend de vite
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        //Metodos que se usan en la API
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Headers que tu frontend envía (Content-Type y Authorization para JWT)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // ✅ Si en algún momento quieres leer headers en el frontend (opcional)
        config.setExposedHeaders(List.of("Authorization"));

        // ⚠️ Importante: si usas cookies/sesión, esto debería ser true.
        // Como vas con JWT stateless, puede ser false (y mejor así).
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // ✅ Aplica CORS a todos los endpoints
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
