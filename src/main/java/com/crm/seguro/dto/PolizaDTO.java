package com.crm.seguro.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PolizaDTO {

    private Long id;
    @NotNull(message = "El tipo de póliza es obligatorio")
    private String tipo;
    @Positive(message = "El monto aseguradi debe ser mayor a 0")
    private double montoAsegurado;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
    @Positive(message = "La prima mensual debe ser mayor a 0")
    private double primaMensual;
    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId; // Evita serializar el objeto Cliente completo
    @NotNull(message = "El agente es obligatorio")
    private Long agenteId; // Evita serializar el objeto Agente completo

}