package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "componentes")
public class Componente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_componente")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "numero_parte", length = 50)
    private String numeroParte;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "ubicacion_en_sistema", length = 100)
    private String ubicacionEnSistema;

    @Column(name = "reemplazable")
    private Boolean reemplazable;

    @Column(name = "tiempo_vida_util")
    private Integer tiempoVidaUtil;

    @Column(name = "fecha_instalacion")
    private LocalDate fechaInstalacion;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoComponente estado;

    @Column(name = "fabricante", length = 100)
    private String fabricante;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @Column(name = "imagen", length = 255)
    private String imagen;

    public enum EstadoComponente {
        OPERATIVO, FALLA, REEMPLAZADO
    }
}