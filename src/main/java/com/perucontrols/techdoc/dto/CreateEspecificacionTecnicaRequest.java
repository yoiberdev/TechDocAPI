package com.perucontrols.techdoc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEspecificacionTecnicaRequest {

    @NotNull(message = "El id del sistema es obligatorio")
    private Long idSistema;

    private String tipoCableado;
    private String voltaje;
    private String amperaje;
    private String protocoloComunicacion;
    private String puertosConexion;
    private String dimensiones;
    private Double peso;
    private String temperaturaOperacion;
    private String proteccionIp;
    private String certificaciones;
    private String requisitosEspeciales;
}
