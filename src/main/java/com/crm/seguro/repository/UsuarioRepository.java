package com.crm.seguro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.seguro.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
