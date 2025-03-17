package com.crm.seguro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "polizas")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private double montoAsegurado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double primaMensual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita problemas de serialización    private Cliente cliente;
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita problemas de serialización    private Agente agente;
    private Agente agente;
}
