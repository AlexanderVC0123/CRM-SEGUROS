package com.crm.seguro.controller;

import com.crm.seguro.dto.PolizaDTO;
import com.crm.seguro.service.PolizaService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.List;


@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*")
public class PolizaController {

    @Autowired
    private PolizaService polizaService;

    @GetMapping
    public Page<PolizaDTO> obtenerPolizas(Pageable pageable) { // Spring Boot detectará automáticamente page, size y sort desde la URL.
        return polizaService.obtenerTodas(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolizaDTO> obtenerPoliza(@PathVariable Long id) {
        return polizaService.obtenerPorId(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PolizaDTO> crearPoliza(@Valid @RequestBody PolizaDTO polizaDTO) {
        return ResponseEntity.ok(polizaDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarPoliza(@PathVariable Long id) {
        polizaService.eliminarPoliza(id);
    }
}
