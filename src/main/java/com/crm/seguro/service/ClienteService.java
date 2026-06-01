package com.crm.seguro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crm.seguro.dto.ClienteDTO;
import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.entity.Usuario;
import com.crm.seguro.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Page<ClienteDTO> obtenerTodos(Pageable pageable){
        return clienteRepository.findAll(pageable)
        .map(DTOMapper::toClienteDTO);
    }

    public Page<ClienteDTO> obtenerTodosPorUsuario(Long usuarioId, Pageable pageable){
        return clienteRepository.findByUsuarioId(usuarioId, pageable)
        .map(DTOMapper::toClienteDTO);
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<ClienteDTO> obtenerPorIdYUsuario(Long id, Long usuarioId) {
        return clienteRepository.findByIdAndUsuarioId(id, usuarioId)
        .map(DTOMapper::toClienteDTO);
    }

    public Cliente guardarCliente(Cliente cliente, Usuario usuario){
        cliente.setUsuario(usuario); // Asociamos el cliente al comercial que esta logueado
        return clienteRepository.save(cliente);
    }

    public Cliente guardarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Cliente existente, Cliente datos, Usuario usuarioAsignado){
        existente.setNombre(datos.getNombre());
        existente.setDniNie(datos.getDniNie());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        existente.setDireccion(datos.getDireccion());
        existente.setUsuario(usuarioAsignado);

        return clienteRepository.save(existente);
    }

    public void eliminarCliente(Long id){
        clienteRepository.deleteById(id);
    }

    public boolean eliminarCliente(Long id, Long usuarioId){
        Optional<Cliente> cliente = clienteRepository.findByIdAndUsuarioId(id, usuarioId);

        if (cliente.isEmpty()) {
            return false;
        }

        clienteRepository.delete(cliente.get());
        return true;
    }

}
