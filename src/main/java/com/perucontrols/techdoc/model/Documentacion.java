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
@Table(name = "documentacion")
public class Documentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "tipo_documento")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "archivo", length = 255)
    private String archivo;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "creado_por", length = 100)
    private String creadoPor;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tags", length = 255)
    private String tags;

    public enum TipoDocumento {
        MANUAL, DIAGRAMA, CERTIFICADO, PROCEDIMIENTO, FOTO
    }
}