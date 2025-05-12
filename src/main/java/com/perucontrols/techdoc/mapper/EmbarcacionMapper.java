package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.CreateEmbarcacionRequest;
import com.perucontrols.techdoc.dto.EmbarcacionDTO;
import com.perucontrols.techdoc.dto.UpdateEmbarcacionRequest;
import com.perucontrols.techdoc.model.Embarcacion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmbarcacionMapper {

    public EmbarcacionDTO toDTO(Embarcacion entity) {
        if (entity == null) return null;

        return new EmbarcacionDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getTipoEmbarcacion(),
                entity.getMatricula(),
                entity.getEmpresaPropietaria(),
                entity.getCapacidadCarga(),
                entity.getFechaConstruccion(),
                entity.getEstado(),
                entity.getUbicacionActual(),
                entity.getFechaRegistro(),
                entity.getNotas()
        );
    }

    public Embarcacion toEntity(CreateEmbarcacionRequest request) {
        Embarcacion entity = new Embarcacion();
        entity.setNombre(request.getNombre());
        entity.setTipoEmbarcacion(request.getTipoEmbarcacion());
        entity.setMatricula(request.getMatricula());
        entity.setEmpresaPropietaria(request.getEmpresaPropietaria());
        entity.setCapacidadCarga(request.getCapacidadCarga());
        entity.setFechaConstruccion(request.getFechaConstruccion());
        entity.setEstado(request.getEstado());
        entity.setUbicacionActual(request.getUbicacionActual());
        entity.setFechaRegistro(LocalDateTime.now());
        entity.setNotas(request.getNotas());
        return entity;
    }

    public void updateEntity(Embarcacion entity, UpdateEmbarcacionRequest request) {
        if (request.getNombre() != null) entity.setNombre(request.getNombre());
        if (request.getTipoEmbarcacion() != null) entity.setTipoEmbarcacion(request.getTipoEmbarcacion());
        if (request.getMatricula() != null) entity.setMatricula(request.getMatricula());
        if (request.getEmpresaPropietaria() != null) entity.setEmpresaPropietaria(request.getEmpresaPropietaria());
        if (request.getCapacidadCarga() != null) entity.setCapacidadCarga(request.getCapacidadCarga());
        if (request.getFechaConstruccion() != null) entity.setFechaConstruccion(request.getFechaConstruccion());
        if (request.getEstado() != null) entity.setEstado(request.getEstado());
        if (request.getUbicacionActual() != null) entity.setUbicacionActual(request.getUbicacionActual());
        if (request.getNotas() != null) entity.setNotas(request.getNotas());
    }
}
