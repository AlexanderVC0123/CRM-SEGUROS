package com.crm.seguro.dto;

import com.crm.seguro.entity.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    private String username;
    private Rol rol;
}
