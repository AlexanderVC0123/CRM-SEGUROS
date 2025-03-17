package com.crm.seguro.controller;

import com.crm.seguro.dto.AgenteDTO;
import com.crm.seguro.entity.Agente;
import com.crm.seguro.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

//import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agentes")
@CrossOrigin(origins = "*")
public class AgenteController {

    @Autowired
    private AgenteService agenteService;

    @GetMapping
    public Page<AgenteDTO> obtenerAgentes(Pageable pageable) {
        return agenteService.obtenerTodos(pageable);
    }

    @GetMapping("/{id}")
    public Optional<Agente> obtenerAgente(@PathVariable Long id) {
        return agenteService.obtenerPorId(id);
    }

    @PostMapping
    public Agente crearAgente(@RequestBody Agente agente) {
        return agenteService.guardarAgente(agente);
    }

    @DeleteMapping("/{id}")
    public void eliminarAgente(@PathVariable Long id) {
        agenteService.eliminarAgente(id);
    }
}

