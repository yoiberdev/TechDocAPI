package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.TipoSistema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTipoSistemaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private TipoSistema.CategoriaSistema categoria;

    private String fabricanteRecomendado;

    private Integer vidaUtilEstimada;
}
