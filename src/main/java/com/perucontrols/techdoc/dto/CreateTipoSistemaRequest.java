package com.perucontrols.techdoc.dto;

import com.perucontrols.techdoc.model.TipoSistema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTipoSistemaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private TipoSistema.CategoriaSistema categoria;

    private String fabricanteRecomendado;

    private Integer vidaUtilEstimada;
}
