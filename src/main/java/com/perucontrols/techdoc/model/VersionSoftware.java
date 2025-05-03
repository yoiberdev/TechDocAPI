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
@Table(name = "versiones_software")
public class VersionSoftware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_version")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "version", nullable = false, length = 50)
    private String version;

    @Column(name = "fecha_instalacion", nullable = false)
    private LocalDate fechaInstalacion;

    @Column(name = "changelog", columnDefinition = "TEXT")
    private String changelog;

    @Column(name = "compatible_con", length = 255)
    private String compatibleCon;

    @Column(name = "archivo_instalador", length = 255)
    private String archivoInstalador;

    @Column(name = "requisitos_sistema", columnDefinition = "TEXT")
    private String requisitosSistema;

    @Column(name = "instalado_por", length = 100)
    private String instaladoPor;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoVersion estado;

    @Column(name = "fecha_fin_soporte")
    private LocalDate fechaFinSoporte;

    public enum EstadoVersion {
        ACTUAL, OBSOLETO, COMPATIBLE
    }
}