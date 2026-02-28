package com.crm.seguro.controller;

import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    private UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }   

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication authentication){
        
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String username = authentication.getName(); //username del UserDetails

        Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD"));
        
        return ResponseEntity.ok(Map.of(
            "id", usuario.getId(),
            "username", usuario.getUsername(),
            "rol", usuario.getRol().name()
        ));
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody Map<String, String> credentials){
        String token = authService.login(credentials.get("username"),credentials.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    //Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> userData){
        Usuario usuario = authService.register(
            userData.get("username"), 
            userData.get("password"), 
            userData.get("rol")
        );
        return ResponseEntity.ok(Map.of(
            "id", usuario.getId(),
            "username", usuario.getUsername(),
            "rol", usuario.getRol()
        ));
    }
}
