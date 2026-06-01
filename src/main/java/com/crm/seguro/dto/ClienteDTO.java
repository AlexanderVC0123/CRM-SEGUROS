package com.crm.seguro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String dniNie;
    private String telefono;
    private String email;
    private String direccion;
    private Long usuarioId;

}
