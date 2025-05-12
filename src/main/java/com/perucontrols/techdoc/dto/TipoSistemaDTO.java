package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.TipoSistema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoSistemaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoSistema.CategoriaSistema categoria;
    private String fabricanteRecomendado;
    private Integer vidaUtilEstimada;
}
