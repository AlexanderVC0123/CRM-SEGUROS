package com.crm.seguro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crm.seguro.dto.AgenteDTO;
import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.entity.Agente;
import com.crm.seguro.repository.AgenteRepository;

@Service
public class AgenteService {

    @Autowired
    private AgenteRepository agenteRepository;

    public Page<AgenteDTO> obtenerTodos(Pageable pageable){
        return agenteRepository.findAll(pageable)
        .map(DTOMapper::toAgenteDTO);
    }

    public Optional<Agente> obtenerPorId(Long id){
        return agenteRepository.findById(id);
    }

    public Agente guardarAgente(Agente agente){
        return agenteRepository.save(agente);
    }

    public Agente actualizarAgente(Agente existente, Agente datos){
        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());

        return agenteRepository.save(existente);
    }

    public void eliminarAgente(Long id){
        agenteRepository.deleteById(id);
    }
}
