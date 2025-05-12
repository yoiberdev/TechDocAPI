package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.SistemaDTO;
import com.perucontrols.techdoc.dto.UpdateSistemaRequest;
import com.perucontrols.techdoc.model.Sistema;
import org.springframework.stereotype.Component;

@Component
public class SistemaMapper {

    public SistemaDTO toDTO(Sistema entity) {
        return new SistemaDTO(
                entity.getId(),
                entity.getEmbarcacion() != null ? entity.getEmbarcacion().getId() : null,
                entity.getTipoSistema() != null ? entity.getTipoSistema().getId() : null,
                entity.getNombre(),
                entity.getNumeroSerie(),
                entity.getFechaInstalacion(),
                entity.getUbicacionEnEmbarcacion(),
                entity.getEstado(),
                entity.getFechaUltimaRevision(),
                entity.getFechaProximoMantenimiento(),
                entity.getTiempoVidaRestante(),
                entity.getTecnicoInstalador(),
                entity.getNotasInstalacion(),
                entity.getDiagramaUbicacion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateEntity(Sistema entity, UpdateSistemaRequest request) {
        if (request.getNombre() != null) entity.setNombre(request.getNombre());
        if (request.getNumeroSerie() != null) entity.setNumeroSerie(request.getNumeroSerie());
        if (request.getFechaInstalacion() != null) entity.setFechaInstalacion(request.getFechaInstalacion());
        if (request.getUbicacionEnEmbarcacion() != null) entity.setUbicacionEnEmbarcacion(request.getUbicacionEnEmbarcacion());
        if (request.getEstado() != null) entity.setEstado(request.getEstado());
        if (request.getFechaUltimaRevision() != null) entity.setFechaUltimaRevision(request.getFechaUltimaRevision());
        if (request.getFechaProximoMantenimiento() != null) entity.setFechaProximoMantenimiento(request.getFechaProximoMantenimiento());
        if (request.getTiempoVidaRestante() != null) entity.setTiempoVidaRestante(request.getTiempoVidaRestante());
        if (request.getTecnicoInstalador() != null) entity.setTecnicoInstalador(request.getTecnicoInstalador());
        if (request.getNotasInstalacion() != null) entity.setNotasInstalacion(request.getNotasInstalacion());
        if (request.getDiagramaUbicacion() != null) entity.setDiagramaUbicacion(request.getDiagramaUbicacion());
    }
}
