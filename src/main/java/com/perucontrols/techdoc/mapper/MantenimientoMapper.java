package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.MantenimientoDTO;
import com.perucontrols.techdoc.dto.UpdateMantenimientoRequest;
import com.perucontrols.techdoc.model.Mantenimiento;
import org.springframework.stereotype.Component;

@Component
public class MantenimientoMapper {

    public MantenimientoDTO toDTO(Mantenimiento entity) {
        return new MantenimientoDTO(
                entity.getId(),
                entity.getSistema() != null ? entity.getSistema().getId() : null,
                entity.getTipo(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getEstado(),
                entity.getRealizadoPor(),
                entity.getDescripcion(),
                entity.getHallazgos(),
                entity.getRecomendaciones(),
                entity.getFechaProximoMantenimiento(),
                entity.getCosto(),
                entity.getTiempoInactividad()
        );
    }

    public void updateEntity(Mantenimiento entity, UpdateMantenimientoRequest request) {
        if (request.getTipo() != null) entity.setTipo(request.getTipo());
        if (request.getFechaInicio() != null) entity.setFechaInicio(request.getFechaInicio());
        if (request.getFechaFin() != null) entity.setFechaFin(request.getFechaFin());
        if (request.getEstado() != null) entity.setEstado(request.getEstado());
        if (request.getRealizadoPor() != null) entity.setRealizadoPor(request.getRealizadoPor());
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getHallazgos() != null) entity.setHallazgos(request.getHallazgos());
        if (request.getRecomendaciones() != null) entity.setRecomendaciones(request.getRecomendaciones());
        if (request.getFechaProximoMantenimiento() != null) entity.setFechaProximoMantenimiento(request.getFechaProximoMantenimiento());
        if (request.getCosto() != null) entity.setCosto(request.getCosto());
        if (request.getTiempoInactividad() != null) entity.setTiempoInactividad(request.getTiempoInactividad());
    }
}
