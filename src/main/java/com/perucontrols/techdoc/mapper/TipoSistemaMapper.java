package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.TipoSistema;
import org.springframework.stereotype.Component;

@Component
public class TipoSistemaMapper {

    public TipoSistemaDTO toDTO(TipoSistema entity) {
        if (entity == null) return null;

        return new TipoSistemaDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getCategoria(),
                entity.getFabricanteRecomendado(),
                entity.getVidaUtilEstimada()
        );
    }

    public TipoSistema toEntity(CreateTipoSistemaRequest request) {
        if (request == null) return null;

        TipoSistema entity = new TipoSistema();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setCategoria(request.getCategoria());
        entity.setFabricanteRecomendado(request.getFabricanteRecomendado());
        entity.setVidaUtilEstimada(request.getVidaUtilEstimada());
        return entity;
    }

    public void updateEntity(TipoSistema entity, UpdateTipoSistemaRequest request) {
        if (entity == null || request == null) return;

        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setCategoria(request.getCategoria());
        entity.setFabricanteRecomendado(request.getFabricanteRecomendado());
        entity.setVidaUtilEstimada(request.getVidaUtilEstimada());
    }
}
