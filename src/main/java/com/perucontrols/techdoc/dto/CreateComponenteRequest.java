package com.perucontrols.techdoc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateComponenteRequest {
    
    @NotNull(message = "El ID del sistema es requerido")
    private Long idSistema;
    
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    
    private String numeroParte;
    
    private String descripcion;
    
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
    
    private String ubicacionEnSistema;
    
    private Boolean reemplazable;
    
    @Positive(message = "El tiempo de vida útil debe ser positivo")
    private Integer tiempoVidaUtil;
    
    private LocalDate fechaInstalacion;
    
    private String estado;
    
    private String fabricante;
    
    private String modelo;
    
    private String imagen;
}