package com.crm.seguro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crm.seguro.entity.Cliente;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    Page<Cliente> findByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Cliente> findByIdAndUsuarioId(Long id, Long usuarioId);

}
