package com.crm.seguro.controller;

import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.dto.PolizaDTO;
import com.crm.seguro.entity.Poliza;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.UsuarioRepository;
import com.crm.seguro.security.JwtUtil;
import com.crm.seguro.service.PolizaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


//import java.util.List;


@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PolizaController {

    @Autowired
    private PolizaService polizaService;

    private final UsuarioRepository usuarioRepository;

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

    @GetMapping
    public Page<PolizaDTO> obtenerPolizas(Pageable pageable, HttpServletRequest request) { 
        Usuario usuario = getUsuarioActual(request);
        return polizaService.obtenerTodasPorUsuario(usuario.getId(),pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolizaDTO> obtenerPoliza(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = getUsuarioActual(request);
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
        Poliza poliza = DTOMapper.toPoliza(polizaDTO, null, null, usuario);
        poliza.setUsuario(usuario);
        Poliza guardada = polizaService.guardarPoliza(poliza);
        return ResponseEntity.ok(DTOMapper.toPolizaDTO(guardada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPoliza(@PathVariable Long id, HttpServletRequest request) {
        Usuario usuario = getUsuarioActual(request);
        polizaService.eliminarPoliza(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }
}
