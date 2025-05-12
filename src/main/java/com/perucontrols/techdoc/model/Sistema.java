package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "sistemas")
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sistema")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_embarcacion", nullable = false)
    private Embarcacion embarcacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_sistema", nullable = false)
    private TipoSistema tipoSistema;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "numero_serie", length = 50)
    private String numeroSerie;

    @Column(name = "fecha_instalacion", nullable = false)
    private LocalDate fechaInstalacion;

    @Column(name = "ubicacion_en_embarcacion", length = 100)
    private String ubicacionEnEmbarcacion;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoSistema estado;

    @Column(name = "fecha_ultima_revision")
    private LocalDate fechaUltimaRevision;

    @Column(name = "fecha_proximo_mantenimiento")
    private LocalDate fechaProximoMantenimiento;

    @Column(name = "tiempo_vida_restante")
    private Integer tiempoVidaRestante;

    @Column(name = "tecnico_instalador", length = 100)
    private String tecnicoInstalador;

    @Column(name = "notas_instalacion", columnDefinition = "TEXT")
    private String notasInstalacion;

    @Column(name = "diagrama_ubicacion", length = 255)
    private String diagramaUbicacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum EstadoSistema {
        OPERATIVO, FALLA, MANTENIMIENTO, DESACTIVADO
    }
}