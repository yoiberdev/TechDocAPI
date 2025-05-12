package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Mantenimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMantenimientoRequest {

    @NotNull
    private Long idSistema;

    private Mantenimiento.TipoMantenimiento tipo;

    @NotNull
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Mantenimiento.EstadoMantenimiento estado;

    @NotBlank
    private String realizadoPor;

    private String descripcion;
    private String hallazgos;
    private String recomendaciones;
    private LocalDate fechaProximoMantenimiento;
    private Double costo;
    private Integer tiempoInactividad;
}
