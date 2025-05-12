package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Sistema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSistemaRequest {

    @NotNull
    private Long idEmbarcacion;

    @NotNull
    private Long idTipoSistema;

    @NotBlank
    private String nombre;

    private String numeroSerie;

    @NotNull
    private LocalDate fechaInstalacion;

    private String ubicacionEnEmbarcacion;
    private Sistema.EstadoSistema estado;
    private LocalDate fechaUltimaRevision;
    private LocalDate fechaProximoMantenimiento;
    private Integer tiempoVidaRestante;
    private String tecnicoInstalador;
    private String notasInstalacion;
    private String diagramaUbicacion;
}
