package com.crm.seguro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;
}
