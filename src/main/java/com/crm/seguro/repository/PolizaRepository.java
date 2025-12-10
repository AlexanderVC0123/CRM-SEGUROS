package com.crm.seguro.repository;


import com.crm.seguro.entity.Poliza;

import java.util.Optional;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {
    Page<Poliza> findByUsuarioId(Long usuarioId, Pageable pageable);
    Optional<Poliza> findByIdAndUsuarioId(Long id, Long usuarioId);
}
