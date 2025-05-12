package com.perucontrols.techdoc.mapper;

import com.perucontrols.techdoc.dto.CreateEspecificacionTecnicaRequest;
import com.perucontrols.techdoc.dto.EspecificacionTecnicaDTO;
import com.perucontrols.techdoc.dto.UpdateEspecificacionTecnicaRequest;
import com.perucontrols.techdoc.model.EspecificacionTecnica;
import org.springframework.stereotype.Component;

@Component
public class EspecificacionTecnicaMapper {

    public EspecificacionTecnicaDTO toDTO(EspecificacionTecnica entity) {
        if (entity == null) return null;

        return new EspecificacionTecnicaDTO(
                entity.getId(),
                entity.getSistema() != null ? entity.getSistema().getId() : null,
                entity.getTipoCableado(),
                entity.getVoltaje(),
                entity.getAmperaje(),
                entity.getProtocoloComunicacion(),
                entity.getPuertosConexion(),
                entity.getDimensiones(),
                entity.getPeso(),
                entity.getTemperaturaOperacion(),
                entity.getProteccionIp(),
                entity.getCertificaciones(),
                entity.getRequisitosEspeciales()
        );
    }

    public void updateEntity(EspecificacionTecnica entity, UpdateEspecificacionTecnicaRequest request) {
        if (request.getTipoCableado() != null) entity.setTipoCableado(request.getTipoCableado());
        if (request.getVoltaje() != null) entity.setVoltaje(request.getVoltaje());
        if (request.getAmperaje() != null) entity.setAmperaje(request.getAmperaje());
        if (request.getProtocoloComunicacion() != null) entity.setProtocoloComunicacion(request.getProtocoloComunicacion());
        if (request.getPuertosConexion() != null) entity.setPuertosConexion(request.getPuertosConexion());
        if (request.getDimensiones() != null) entity.setDimensiones(request.getDimensiones());
        if (request.getPeso() != null) entity.setPeso(request.getPeso());
        if (request.getTemperaturaOperacion() != null) entity.setTemperaturaOperacion(request.getTemperaturaOperacion());
        if (request.getProteccionIp() != null) entity.setProteccionIp(request.getProteccionIp());
        if (request.getCertificaciones() != null) entity.setCertificaciones(request.getCertificaciones());
        if (request.getRequisitosEspeciales() != null) entity.setRequisitosEspeciales(request.getRequisitosEspeciales());
    }
}
