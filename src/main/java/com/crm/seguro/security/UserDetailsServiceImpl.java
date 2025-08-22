package com.crm.seguro.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado " + username));

        return org.springframework.security.core.userdetails.User
            .withUsername(usuario.getUsername())
            .password(usuario.getPassword())
            .roles(usuario.getRol().name())  // Esto asigna el rol de usuario
            .build();
        //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}
