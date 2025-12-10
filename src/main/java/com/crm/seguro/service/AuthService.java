package com.crm.seguro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crm.seguro.entity.Rol;
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

    //Método para registrar usuarios
    public Usuario register(String username, String password, String rol){

        // Validar que el rol sea correcto (ADMIN, AGENTE, CLIENTE)
        if (!List.of("ADMIN", "AGENTE", "CLIENTE").contains(rol.toUpperCase())){
            throw new RuntimeException("Rol inválido");
        }

        //Validar que no exista otro usuario con el mismo username
        if(usuarioRepository.findByUsername(username).isPresent()){
             throw new RuntimeException("El usuario ya existe.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password)); //Gerena el hash
        usuario.setRol(Rol.valueOf(rol.toUpperCase()));

        return usuarioRepository.save(usuario); //Guarda en la BD


    }
}
