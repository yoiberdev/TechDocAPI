package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoMantenimiento tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoMantenimiento estado;

    @Column(name = "realizado_por", nullable = false, length = 100)
    private String realizadoPor;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "hallazgos", columnDefinition = "TEXT")
    private String hallazgos;

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "fecha_proximo_mantenimiento")
    private LocalDate fechaProximoMantenimiento;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "tiempo_inactividad")
    private Integer tiempoInactividad;

    public enum TipoMantenimiento {
        PREVENTIVO, CORRECTIVO, ACTUALIZACION
    }

    public enum EstadoMantenimiento {
        PLANIFICADO, EN_PROGRESO, COMPLETADO, CANCELADO
    }
}