package com.crm.seguro.repository;


import com.crm.seguro.entity.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {

}
