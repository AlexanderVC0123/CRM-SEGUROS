package com.crm.seguro.controller;

import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.dto.UsuarioDTO;
import com.crm.seguro.dto.UsuarioRequestDTO;
import com.crm.seguro.entity.Rol;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private Usuario getUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private void validarAdmin(Authentication authentication) {
        Usuario usuario = getUsuarioActual(authentication);

        if (usuario.getRol() != Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el admin puede gestionar usuarios");
        }
    }

    private Rol leerRol(String rol) {
        try {
            return Rol.valueOf(rol.toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido");
        }
    }

    @GetMapping
    public Page<UsuarioDTO> obtenerUsuarios(Pageable pageable, Authentication authentication) {
        validarAdmin(authentication);
        return usuarioRepository.findAll(pageable).map(DTOMapper::toUsuarioDTO);
    }

    @GetMapping("/comerciales")
    public Page<UsuarioDTO> obtenerComerciales(Pageable pageable, Authentication authentication) {
        validarAdmin(authentication);
        return usuarioRepository.findByRol(Rol.AGENTE, pageable).map(DTOMapper::toUsuarioDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id, Authentication authentication) {
        validarAdmin(authentication);
        return usuarioRepository.findById(id)
                .map(DTOMapper::toUsuarioDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioRequestDTO datos, Authentication authentication) {
        validarAdmin(authentication);

        if (usuarioRepository.findByUsername(datos.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(datos.getUsername());
        usuario.setPassword(passwordEncoder.encode(datos.getPassword()));
        usuario.setRol(leerRol(datos.getRol()));

        return ResponseEntity.ok(DTOMapper.toUsuarioDTO(usuarioRepository.save(usuario)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO datos, Authentication authentication) {
        validarAdmin(authentication);

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setUsername(datos.getUsername());
                    usuario.setRol(leerRol(datos.getRol()));

                    // Si no se manda password, mantenemos la contrasena actual.
                    if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                        usuario.setPassword(passwordEncoder.encode(datos.getPassword()));
                    }

                    return usuarioRepository.save(usuario);
                })
                .map(DTOMapper::toUsuarioDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, Authentication authentication) {
        validarAdmin(authentication);
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
