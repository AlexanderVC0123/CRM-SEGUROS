package com.crm.seguro.controller;

import com.crm.seguro.entity.Usuario;
import com.crm.seguro.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

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
