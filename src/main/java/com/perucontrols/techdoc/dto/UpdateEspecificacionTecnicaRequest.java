package com.perucontrols.techdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEspecificacionTecnicaRequest {

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
