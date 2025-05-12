package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Embarcacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmbarcacionRequest {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    private String tipoEmbarcacion;

    @NotBlank(message = "La matrícula es requerida")
    private String matricula;

    private String empresaPropietaria;

    private Double capacidadCarga;

    private LocalDate fechaConstruccion;

    private Embarcacion.EstadoEmbarcacion estado;

    private String ubicacionActual;

    private String notas;
}
