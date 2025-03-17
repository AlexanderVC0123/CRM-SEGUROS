package com.crm.seguro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crm.seguro.dto.ClienteDTO;
import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Page<ClienteDTO> obtenerTodos(Pageable pageable){
        return clienteRepository.findAll(pageable)
        .map(DTOMapper::toClienteDTO);
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id){
        clienteRepository.deleteById(id);
    }

}
