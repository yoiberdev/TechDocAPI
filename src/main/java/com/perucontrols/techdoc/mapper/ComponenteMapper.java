package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Componente;
import org.springframework.stereotype.Component;

@Component
public class ComponenteMapper {
    
    public ComponenteDTO toDTO(Componente entity) {
        if (entity == null) {
            return null;
        }
        
        ComponenteDTO dto = new ComponenteDTO();
        dto.setId(entity.getId());
        dto.setIdSistema(entity.getSistema() != null ? entity.getSistema().getId() : null);
        dto.setNombre(entity.getNombre());
        dto.setNumeroParte(entity.getNumeroParte());
        dto.setDescripcion(entity.getDescripcion());
        dto.setCantidad(entity.getCantidad());
        dto.setUbicacionEnSistema(entity.getUbicacionEnSistema());
        dto.setReemplazable(entity.getReemplazable());
        dto.setTiempoVidaUtil(entity.getTiempoVidaUtil());
        dto.setFechaInstalacion(entity.getFechaInstalacion());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);
        dto.setFabricante(entity.getFabricante());
        dto.setModelo(entity.getModelo());
        dto.setImagen(entity.getImagen());
        
        return dto;
    }
    
    public Componente toEntity(CreateComponenteRequest request) {
        if (request == null) {
            return null;
        }
        
        Componente entity = new Componente();
        entity.setNombre(request.getNombre());
        entity.setNumeroParte(request.getNumeroParte());
        entity.setDescripcion(request.getDescripcion());
        entity.setCantidad(request.getCantidad());
        entity.setUbicacionEnSistema(request.getUbicacionEnSistema());
        entity.setReemplazable(request.getReemplazable());
        entity.setTiempoVidaUtil(request.getTiempoVidaUtil());
        entity.setFechaInstalacion(request.getFechaInstalacion());
        entity.setFabricante(request.getFabricante());
        entity.setModelo(request.getModelo());
        entity.setImagen(request.getImagen());
        
        return entity;
    }
    
    public void updateEntity(Componente entity, UpdateComponenteRequest request) {
        if (entity == null || request == null) {
            return;
        }
        
        entity.setNombre(request.getNombre());
        
        if (request.getNumeroParte() != null) {
            entity.setNumeroParte(request.getNumeroParte());
        }
        
        if (request.getDescripcion() != null) {
            entity.setDescripcion(request.getDescripcion());
        }
        
        if (request.getCantidad() != null) {
            entity.setCantidad(request.getCantidad());
        }
        
        if (request.getUbicacionEnSistema() != null) {
            entity.setUbicacionEnSistema(request.getUbicacionEnSistema());
        }
        
        if (request.getReemplazable() != null) {
            entity.setReemplazable(request.getReemplazable());
        }
        
        if (request.getTiempoVidaUtil() != null) {
            entity.setTiempoVidaUtil(request.getTiempoVidaUtil());
        }
        
        if (request.getFechaInstalacion() != null) {
            entity.setFechaInstalacion(request.getFechaInstalacion());
        }
        
        if (request.getFabricante() != null) {
            entity.setFabricante(request.getFabricante());
        }
        
        if (request.getModelo() != null) {
            entity.setModelo(request.getModelo());
        }
        
        if (request.getImagen() != null) {
            entity.setImagen(request.getImagen());
        }
    }
}