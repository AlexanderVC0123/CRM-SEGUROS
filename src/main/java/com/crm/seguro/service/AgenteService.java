package com.crm.seguro.service;

import java.util.List;
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

    public void eliminarAgente(Long id){
        agenteRepository.deleteById(id);
    }
}
