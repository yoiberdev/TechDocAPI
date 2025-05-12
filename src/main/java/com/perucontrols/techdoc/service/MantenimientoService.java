package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.MantenimientoMapper;
import com.perucontrols.techdoc.model.Mantenimiento;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.MantenimientoRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MantenimientoService {

    private final MantenimientoRepository repository;
    private final SistemaRepository sistemaRepository;
    private final MantenimientoMapper mapper;

    @Transactional(readOnly = true)
    public List<MantenimientoDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<MantenimientoDTO> getAllPaged(Pageable pageable) {
        Page<MantenimientoDTO> page = repository.findAll(pageable)
                .map(mapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public MantenimientoDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado: " + id)));
    }

    @Transactional
    public MantenimientoDTO create(CreateMantenimientoRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado: " + request.getIdSistema()));

        Mantenimiento entity = new Mantenimiento();
        entity.setSistema(sistema);
        entity.setTipo(request.getTipo());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        entity.setEstado(request.getEstado());
        entity.setRealizadoPor(request.getRealizadoPor());
        entity.setDescripcion(request.getDescripcion());
        entity.setHallazgos(request.getHallazgos());
        entity.setRecomendaciones(request.getRecomendaciones());
        entity.setFechaProximoMantenimiento(request.getFechaProximoMantenimiento());
        entity.setCosto(request.getCosto());
        entity.setTiempoInactividad(request.getTiempoInactividad());

        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public MantenimientoDTO update(Long id, UpdateMantenimientoRequest request) {
        Mantenimiento entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento no encontrado: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Mantenimiento no encontrado: " + id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MantenimientoDTO> getBySistema(Long idSistema) {
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado: " + idSistema));
        return repository.findBySistema(sistema).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MantenimientoDTO> getBySistemaAndEstado(Long idSistema, Mantenimiento.EstadoMantenimiento estado) {
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado: " + idSistema));
        return repository.findBySistemaAndEstado(sistema, estado).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MantenimientoDTO> getByRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaInicioAfterAndFechaInicioBefore(inicio, fin).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MantenimientoDTO> getByTipo(Mantenimiento.TipoMantenimiento tipo) {
        return repository.findByTipo(tipo).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
