package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Documentacion;
import org.springframework.stereotype.Component;

@Component
public class DocumentacionMapper {

    public DocumentacionDTO toDTO(Documentacion entity) {
        if (entity == null) return null;

        return new DocumentacionDTO(
                entity.getId(),
                entity.getSistema() != null ? entity.getSistema().getId() : null,
                entity.getTipoDocumento(),
                entity.getTitulo(),
                entity.getArchivo(),
                entity.getFechaCreacion(),
                entity.getCreadoPor(),
                entity.getVersion(),
                entity.getDescripcion(),
                entity.getTags()
        );
    }

    public Documentacion toEntity(CreateDocumentacionRequest request) {
        if (request == null) return null;

        Documentacion entity = new Documentacion();
        entity.setTipoDocumento(request.getTipoDocumento());
        entity.setTitulo(request.getTitulo());
        entity.setArchivo(request.getArchivo());
        entity.setFechaCreacion(request.getFechaCreacion());
        entity.setCreadoPor(request.getCreadoPor());
        entity.setVersion(request.getVersion());
        entity.setDescripcion(request.getDescripcion());
        entity.setTags(request.getTags());

        return entity;
    }

    public void updateEntity(Documentacion entity, UpdateDocumentacionRequest request) {
        if (entity == null || request == null) return;

        if (request.getTipoDocumento() != null) entity.setTipoDocumento(request.getTipoDocumento());
        if (request.getTitulo() != null) entity.setTitulo(request.getTitulo());
        if (request.getArchivo() != null) entity.setArchivo(request.getArchivo());
        if (request.getFechaCreacion() != null) entity.setFechaCreacion(request.getFechaCreacion());
        if (request.getCreadoPor() != null) entity.setCreadoPor(request.getCreadoPor());
        if (request.getVersion() != null) entity.setVersion(request.getVersion());
        if (request.getDescripcion() != null) entity.setDescripcion(request.getDescripcion());
        if (request.getTags() != null) entity.setTags(request.getTags());
    }
}
