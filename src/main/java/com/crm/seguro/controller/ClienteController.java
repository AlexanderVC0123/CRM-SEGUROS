package com.crm.seguro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.seguro.dto.ClienteDTO;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") //Permitir accesso desde frontend
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public Page<ClienteDTO> obtenerClientes(Pageable pageable){
        return clienteService.obtenerTodos(pageable);
    }

    @GetMapping("/{id}")
    public Optional<Cliente> obtenerCliente(@PathVariable Long id){
        return clienteService.obtenerPorId(id);
    }

    @PostMapping
    public Cliente crearCliente(@RequestBody Cliente cliente){
        return clienteService.guardarCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);
    }

}
