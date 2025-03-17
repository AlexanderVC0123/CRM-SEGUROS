package com.crm.seguro.dto;

import com.crm.seguro.entity.Agente;
import com.crm.seguro.entity.Cliente;
import com.crm.seguro.entity.Poliza;

public class DTOMapper {

    public static PolizaDTO toPolizaDTO(Poliza poliza){
        PolizaDTO dto = new PolizaDTO();
        dto.setId(poliza.getId());
        dto.setTipo(poliza.getTipo());
        dto.setMontoAsegurado(poliza.getMontoAsegurado());
        dto.setFechaInicio(poliza.getFechaInicio());
        dto.setFechaFin(poliza.getFechaFin());
        dto.setPrimaMensual(poliza.getPrimaMensual());
        dto.setClienteId(poliza.getCliente().getId());
        dto.setAgenteId(poliza.getAgente().getId());

        return dto;
    }

    public static ClienteDTO toClienteDTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }

    public static AgenteDTO toAgenteDTO(Agente agente) {
        AgenteDTO dto = new AgenteDTO();
        dto.setId(agente.getId());
        dto.setNombre(agente.getNombre());
        dto.setEmail(agente.getEmail());
        dto.setTelefono(agente.getTelefono());
        return dto;
    }
    
}
