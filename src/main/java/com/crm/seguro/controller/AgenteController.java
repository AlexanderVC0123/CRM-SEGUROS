package com.crm.seguro.controller;

import com.crm.seguro.dto.AgenteDTO;
import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.entity.Agente;
import com.crm.seguro.entity.Rol;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.service.AgenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/agentes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AgenteController {

    private final AgenteService agenteService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private void validarAdmin(Authentication authentication) {
        Usuario usuario = getUsuarioActual(authentication);

        if (usuario.getRol() != Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el admin puede gestionar comerciales");
        }
    }

    @GetMapping
    public Page<AgenteDTO> obtenerAgentes(Pageable pageable, Authentication authentication) {
        validarAdmin(authentication);
        return agenteService.obtenerTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenteDTO> obtenerAgente(@PathVariable Long id, Authentication authentication) {
        validarAdmin(authentication);
        return agenteService.obtenerPorId(id)
                .map(DTOMapper::toAgenteDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgenteDTO> crearAgente(@RequestBody Agente agente, Authentication authentication) {
        validarAdmin(authentication);
        Agente guardado = agenteService.guardarAgente(agente);
        return ResponseEntity.ok(DTOMapper.toAgenteDTO(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgenteDTO> actualizarAgente(@PathVariable Long id, @RequestBody Agente datos, Authentication authentication) {
        validarAdmin(authentication);

        return agenteService.obtenerPorId(id)
                .map(agente -> agenteService.actualizarAgente(agente, datos))
                .map(DTOMapper::toAgenteDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAgente(@PathVariable Long id, Authentication authentication) {
        validarAdmin(authentication);
        agenteService.eliminarAgente(id);
        return ResponseEntity.noContent().build();
    }
}
