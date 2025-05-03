package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "especificaciones_tecnicas")
public class EspecificacionTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especificacion")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "tipo_cableado", length = 100)
    private String tipoCableado;

    @Column(name = "voltaje", length = 50)
    private String voltaje;

    @Column(name = "amperaje", length = 50)
    private String amperaje;

    @Column(name = "protocolo_comunicacion", length = 100)
    private String protocoloComunicacion;

    @Column(name = "puertos_conexion", columnDefinition = "TEXT")
    private String puertosConexion;

    @Column(name = "dimensiones", length = 100)
    private String dimensiones;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "temperatura_operacion", length = 50)
    private String temperaturaOperacion;

    @Column(name = "proteccion_ip", length = 20)
    private String proteccionIp;

    @Column(name = "certificaciones", columnDefinition = "TEXT")
    private String certificaciones;

    @Column(name = "requisitos_especiales", columnDefinition = "TEXT")
    private String requisitosEspeciales;
}