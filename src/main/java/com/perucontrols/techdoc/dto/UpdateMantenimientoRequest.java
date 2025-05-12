package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Mantenimiento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMantenimientoRequest {

    private Mantenimiento.TipoMantenimiento tipo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Mantenimiento.EstadoMantenimiento estado;
    private String realizadoPor;
    private String descripcion;
    private String hallazgos;
    private String recomendaciones;
    private LocalDate fechaProximoMantenimiento;
    private Double costo;
    private Integer tiempoInactividad;
}
