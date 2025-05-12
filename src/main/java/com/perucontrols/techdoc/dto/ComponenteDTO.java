package com.perucontrols.techdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponenteDTO {
    private Long id;
    private Long idSistema;
    private String nombre;
    private String numeroParte;
    private String descripcion;
    private Integer cantidad;
    private String ubicacionEnSistema;
    private Boolean reemplazable;
    private Integer tiempoVidaUtil;
    private LocalDate fechaInstalacion;
    private String estado;
    private String fabricante;
    private String modelo;
    private String imagen;
}