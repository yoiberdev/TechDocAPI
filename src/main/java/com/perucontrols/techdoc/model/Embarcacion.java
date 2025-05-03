package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "embarcaciones")
public class Embarcacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_embarcacion")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_embarcacion", length = 50)
    private String tipoEmbarcacion;

    @Column(name = "matricula", nullable = false, unique = true, length = 50)
    private String matricula;

    @Column(name = "empresa_propietaria", length = 100)
    private String empresaPropietaria;

    @Column(name = "capacidad_carga")
    private Double capacidadCarga;

    @Column(name = "fecha_construccion")
    private LocalDate fechaConstruccion;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoEmbarcacion estado;

    @Column(name = "ubicacion_actual", length = 100)
    private String ubicacionActual;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    public enum EstadoEmbarcacion {
        ACTIVO, INACTIVO, MANTENIMIENTO
    }
}