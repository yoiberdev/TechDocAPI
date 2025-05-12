package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.EspecificacionTecnicaMapper;
import com.perucontrols.techdoc.model.EspecificacionTecnica;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.EspecificacionTecnicaRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EspecificacionTecnicaService {

    private final EspecificacionTecnicaRepository repository;
    private final SistemaRepository sistemaRepository;
    private final EspecificacionTecnicaMapper mapper;

    @Transactional(readOnly = true)
    public List<EspecificacionTecnicaDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecificacionTecnicaDTO getById(Long id) {
        EspecificacionTecnica entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especificación no encontrada con ID: " + id));
        return mapper.toDTO(entity);
    }

    @Transactional
    public EspecificacionTecnicaDTO create(CreateEspecificacionTecnicaRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + request.getIdSistema()));

        Optional<EspecificacionTecnica> existing = repository.findBySistema(sistema);
        if (existing.isPresent())
            throw new IllegalArgumentException("Ya existe una especificación técnica para este sistema");

        EspecificacionTecnica entity = new EspecificacionTecnica();
        entity.setSistema(sistema);
        entity.setTipoCableado(request.getTipoCableado());
        entity.setVoltaje(request.getVoltaje());
        entity.setAmperaje(request.getAmperaje());
        entity.setProtocoloComunicacion(request.getProtocoloComunicacion());
        entity.setPuertosConexion(request.getPuertosConexion());
        entity.setDimensiones(request.getDimensiones());
        entity.setPeso(request.getPeso());
        entity.setTemperaturaOperacion(request.getTemperaturaOperacion());
        entity.setProteccionIp(request.getProteccionIp());
        entity.setCertificaciones(request.getCertificaciones());
        entity.setRequisitosEspeciales(request.getRequisitosEspeciales());

        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public EspecificacionTecnicaDTO update(Long id, UpdateEspecificacionTecnicaRequest request) {
        EspecificacionTecnica entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especificación no encontrada con ID: " + id));

        mapper.updateEntity(entity, request);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Especificación no encontrada con ID: " + id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EspecificacionTecnicaDTO getBySistema(Long sistemaId) {
        Sistema sistema = sistemaRepository.findById(sistemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + sistemaId));

        EspecificacionTecnica entity = repository.findBySistema(sistema)
                .orElseThrow(() -> new ResourceNotFoundException("No existe especificación técnica para el sistema"));

        return mapper.toDTO(entity);
    }
}
