package com.perucontrols.techdoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_sistema")
public class TipoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_sistema")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaSistema categoria;

    @Column(name = "fabricante_recomendado", length = 100)
    private String fabricanteRecomendado;

    @Column(name = "vida_util_estimada")
    private Integer vidaUtilEstimada;

    public enum CategoriaSistema {
        MONITOREO, CONTROL, NAVEGACION, COMUNICACION, REFRIGERACION, OTRO
    }
}