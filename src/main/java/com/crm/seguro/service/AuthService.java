package com.crm.seguro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password){
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if (usuario.isPresent() && passwordEncoder.matches(password,usuario.get().getPassword())) {
            return jwtUtil.generateToken(username);
        }
        throw new RuntimeException("Credenciales inválidas");
    }
}
