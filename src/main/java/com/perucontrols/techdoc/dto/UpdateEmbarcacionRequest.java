package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Embarcacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmbarcacionRequest {
    private String nombre;
    private String tipoEmbarcacion;
    private String matricula;
    private String empresaPropietaria;
    private Double capacidadCarga;
    private LocalDate fechaConstruccion;
    private Embarcacion.EstadoEmbarcacion estado;
    private String ubicacionActual;
    private String notas;
}
