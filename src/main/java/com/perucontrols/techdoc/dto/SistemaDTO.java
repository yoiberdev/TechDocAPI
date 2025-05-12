package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.Sistema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SistemaDTO {
    private Long id;
    private Long idEmbarcacion;
    private Long idTipoSistema;
    private String nombre;
    private String numeroSerie;
    private LocalDate fechaInstalacion;
    private String ubicacionEnEmbarcacion;
    private Sistema.EstadoSistema estado;
    private LocalDate fechaUltimaRevision;
    private LocalDate fechaProximoMantenimiento;
    private Integer tiempoVidaRestante;
    private String tecnicoInstalador;
    private String notasInstalacion;
    private String diagramaUbicacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
