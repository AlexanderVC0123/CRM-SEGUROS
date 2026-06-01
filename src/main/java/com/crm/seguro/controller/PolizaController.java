package com.crm.seguro.controller;

import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.dto.PolizaDTO;
import com.crm.seguro.entity.Agente;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.entity.Poliza;
import com.crm.seguro.entity.Rol;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.AgenteRepository;
import com.crm.seguro.repository.ClienteRepository;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.security.JwtUtil;
import com.crm.seguro.service.PolizaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


//import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*")
public class PolizaController {

    
    private final PolizaService polizaService;

    private final UsuarioRepository usuarioRepository;

    private final ClienteRepository clienteRepository;

    private final AgenteRepository agenteRepository;

    private final JwtUtil jwtUtil;

    private Usuario getUsuarioActual(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado"); 
        }
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private boolean esAdmin(Usuario usuario) {
        return usuario.getRol() == Rol.ADMIN;
    }

    @GetMapping
    public Page<PolizaDTO> obtenerPolizas(Pageable pageable, HttpServletRequest request) { 
        Usuario usuario = getUsuarioActual(request);

        if (esAdmin(usuario)) {
            return polizaService.obtenerTodas(pageable);
        }

        return polizaService.obtenerTodasPorUsuario(usuario.getId(),pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolizaDTO> obtenerPoliza(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = getUsuarioActual(request);

        if (esAdmin(usuario)) {
            return polizaService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
        }

        return polizaService.obtenerPorIdYUsuario(id, usuario.getId())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/test")
    public ResponseEntity<?> testPoliza(){
        return ResponseEntity.ok(Map.of(
            "mensaje", "Acceso autorizado a polizas"
        ));
    }

    @PostMapping
    public ResponseEntity<PolizaDTO> crearPoliza(@Valid @RequestBody PolizaDTO polizaDTO, HttpServletRequest request) {
        Usuario usuario = getUsuarioActual(request);

        // Buscamos el cliente que viene desde el formulario por su id.
        Cliente cliente;
        if (esAdmin(usuario)) {
            cliente = clienteRepository.findById(polizaDTO.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        } else {
            cliente = clienteRepository.findByIdAndUsuarioId(polizaDTO.getClienteId(), usuario.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        }

        // Buscamos el agente que viene desde el formulario por su id.
        Agente agente = agenteRepository.findById(polizaDTO.getAgenteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agente no encontrado"));

        // Convertimos el DTO en entidad completa antes de guardarla en la base de datos.
        Poliza poliza = DTOMapper.toPoliza(polizaDTO, cliente, agente, usuario);

        Poliza guardada = polizaService.guardarPoliza(poliza);
        return ResponseEntity.ok(DTOMapper.toPolizaDTO(guardada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPoliza(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = getUsuarioActual(request);

        if (esAdmin(usuario)) {
            polizaService.eliminarPoliza(id);
            return ResponseEntity.noContent().build();
        }

        polizaService.eliminarPoliza(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
}
