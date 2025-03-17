package com.crm.seguro.service;

import com.crm.seguro.dto.DTOMapper;
import com.crm.seguro.dto.PolizaDTO;
import com.crm.seguro.entity.Poliza;
import com.crm.seguro.repository.PolizaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

//import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

//import javax.swing.Spring;

@Service
public class PolizaService {

    @Autowired
    private PolizaRepository polizaRepository;


    public Page<PolizaDTO> obtenerTodas(Pageable pageable) {
        return polizaRepository.findAll(pageable) //Ahora el método devuelve Page<PolizaDTO> en lugar de List<PolizaDTO>.
                .map(DTOMapper::toPolizaDTO);  //Spring manejará automáticamente la paginación.
    }

    public Optional<PolizaDTO> obtenerPorId(Long id) {
        return polizaRepository.findById(id).map(DTOMapper::toPolizaDTO);
    }

    public Poliza guardarPoliza(Poliza poliza) {
        return polizaRepository.save(poliza);
    }

    public void eliminarPoliza(Long id) {
        polizaRepository.deleteById(id);
    }
}

