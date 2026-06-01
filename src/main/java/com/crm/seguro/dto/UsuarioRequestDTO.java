package com.crm.seguro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {
    private String username;
    private String password;
    private String rol;
}
