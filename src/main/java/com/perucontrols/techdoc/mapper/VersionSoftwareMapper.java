package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.CreateVersionSoftwareRequest;
import com.perucontrols.techdoc.dto.UpdateVersionSoftwareRequest;
import com.perucontrols.techdoc.dto.VersionSoftwareDTO;
import com.perucontrols.techdoc.model.VersionSoftware;
import org.springframework.stereotype.Component;

@Component
public class VersionSoftwareMapper {

    public VersionSoftwareDTO toDTO(VersionSoftware entity) {
        if (entity == null) return null;

        return new VersionSoftwareDTO(
                entity.getId(),
                entity.getSistema() != null ? entity.getSistema().getId() : null,
                entity.getVersion(),
                entity.getFechaInstalacion(),
                entity.getChangelog(),
                entity.getCompatibleCon(),
                entity.getArchivoInstalador(),
                entity.getRequisitosSistema(),
                entity.getInstaladoPor(),
                entity.getEstado(),
                entity.getFechaFinSoporte()
        );
    }

    public VersionSoftware toEntity(CreateVersionSoftwareRequest request) {
        VersionSoftware entity = new VersionSoftware();
        entity.setVersion(request.getVersion());
        entity.setFechaInstalacion(request.getFechaInstalacion());
        entity.setChangelog(request.getChangelog());
        entity.setCompatibleCon(request.getCompatibleCon());
        entity.setArchivoInstalador(request.getArchivoInstalador());
        entity.setRequisitosSistema(request.getRequisitosSistema());
        entity.setInstaladoPor(request.getInstaladoPor());
        entity.setEstado(request.getEstado());
        entity.setFechaFinSoporte(request.getFechaFinSoporte());
        return entity;
    }

    public void updateEntity(VersionSoftware entity, UpdateVersionSoftwareRequest request) {
        if (request.getVersion() != null) entity.setVersion(request.getVersion());
        if (request.getFechaInstalacion() != null) entity.setFechaInstalacion(request.getFechaInstalacion());
        if (request.getChangelog() != null) entity.setChangelog(request.getChangelog());
        if (request.getCompatibleCon() != null) entity.setCompatibleCon(request.getCompatibleCon());
        if (request.getArchivoInstalador() != null) entity.setArchivoInstalador(request.getArchivoInstalador());
        if (request.getRequisitosSistema() != null) entity.setRequisitosSistema(request.getRequisitosSistema());
        if (request.getInstaladoPor() != null) entity.setInstaladoPor(request.getInstaladoPor());
        if (request.getEstado() != null) entity.setEstado(request.getEstado());
        if (request.getFechaFinSoporte() != null) entity.setFechaFinSoporte(request.getFechaFinSoporte());
    }
}
