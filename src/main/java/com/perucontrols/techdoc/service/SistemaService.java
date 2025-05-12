package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.SistemaMapper;
import com.perucontrols.techdoc.model.*;
import com.perucontrols.techdoc.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SistemaService {

    private final SistemaRepository sistemaRepository;
    private final EmbarcacionRepository embarcacionRepository;
    private final TipoSistemaRepository tipoSistemaRepository;
    private final SistemaMapper sistemaMapper;

    @Transactional(readOnly = true)
    public List<SistemaDTO> getAllSistemas() {
        return sistemaRepository.findAll()
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<SistemaDTO> getAllSistemasPaged(Pageable pageable) {
        Page<SistemaDTO> page = sistemaRepository.findAll(pageable)
                .map(sistemaMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public SistemaDTO getSistemaById(Long id) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + id));
        return sistemaMapper.toDTO(sistema);
    }

    @Transactional
    public SistemaDTO createSistema(CreateSistemaRequest request) {
        Embarcacion embarcacion = embarcacionRepository.findById(request.getIdEmbarcacion())
                .orElseThrow(() -> new ResourceNotFoundException("Embarcación no encontrada con ID: " + request.getIdEmbarcacion()));

        TipoSistema tipoSistema = tipoSistemaRepository.findById(request.getIdTipoSistema())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de sistema no encontrado con ID: " + request.getIdTipoSistema()));

        Sistema sistema = new Sistema();
        sistema.setEmbarcacion(embarcacion);
        sistema.setTipoSistema(tipoSistema);
        sistema.setNombre(request.getNombre());
        sistema.setNumeroSerie(request.getNumeroSerie());
        sistema.setFechaInstalacion(request.getFechaInstalacion());
        sistema.setUbicacionEnEmbarcacion(request.getUbicacionEnEmbarcacion());
        sistema.setEstado(request.getEstado());
        sistema.setFechaUltimaRevision(request.getFechaUltimaRevision());
        sistema.setFechaProximoMantenimiento(request.getFechaProximoMantenimiento());
        sistema.setTiempoVidaRestante(request.getTiempoVidaRestante());
        sistema.setTecnicoInstalador(request.getTecnicoInstalador());
        sistema.setNotasInstalacion(request.getNotasInstalacion());
        sistema.setDiagramaUbicacion(request.getDiagramaUbicacion());

        Sistema saved = sistemaRepository.save(sistema);
        return sistemaMapper.toDTO(saved);
    }

    @Transactional
    public SistemaDTO updateSistema(Long id, UpdateSistemaRequest request) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + id));

        sistemaMapper.updateEntity(sistema, request);
        Sistema updated = sistemaRepository.save(sistema);
        return sistemaMapper.toDTO(updated);
    }

    @Transactional
    public void deleteSistema(Long id) {
        if (!sistemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sistema no encontrado con ID: " + id);
        }
        sistemaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SistemaDTO> getSistemasByEmbarcacion(Long idEmbarcacion) {
        Embarcacion embarcacion = embarcacionRepository.findById(idEmbarcacion)
                .orElseThrow(() -> new ResourceNotFoundException("Embarcación no encontrada con ID: " + idEmbarcacion));
        return sistemaRepository.findByEmbarcacion(embarcacion)
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SistemaDTO> getSistemasByTipoSistema(Long idTipoSistema) {
        TipoSistema tipoSistema = tipoSistemaRepository.findById(idTipoSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de sistema no encontrado con ID: " + idTipoSistema));
        return sistemaRepository.findByTipoSistema(tipoSistema)
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SistemaDTO> getSistemasByEstado(Sistema.EstadoSistema estado) {
        return sistemaRepository.findByEstado(estado)
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SistemaDTO> getSistemasRequiringMaintenance() {
        return sistemaRepository.findSistemasRequiringMaintenance(LocalDate.now())
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SistemaDTO> getSistemasByNombre(String nombre) {
        return sistemaRepository.findByNombreContaining(nombre)
                .stream()
                .map(sistemaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
