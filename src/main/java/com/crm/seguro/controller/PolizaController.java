package com.crm.seguro.controller;

import com.crm.seguro.entity.Poliza;
import com.crm.seguro.service.PolizaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*")
public class PolizaController {

    @Autowired
    private PolizaService polizaService;

    @GetMapping
    public List<Poliza> obtenerPolizas() {
        return polizaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Poliza> obtenerPoliza(@PathVariable Long id) {
        return polizaService.obtenerPorId(id);
    }

    @PostMapping
    public Poliza crearPoliza(@RequestBody Poliza poliza) {
        return polizaService.guardarPoliza(poliza);
    }

    @DeleteMapping("/{id}")
    public void eliminarPoliza(@PathVariable Long id) {
        polizaService.eliminarPoliza(id);
    }
}
