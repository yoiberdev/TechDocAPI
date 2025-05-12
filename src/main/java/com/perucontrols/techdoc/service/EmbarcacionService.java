package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.EmbarcacionMapper;
import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.repository.EmbarcacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbarcacionService {

    private final EmbarcacionRepository embarcacionRepository;
    private final EmbarcacionMapper embarcacionMapper;

    @Transactional(readOnly = true)
    public List<EmbarcacionDTO> getAll() {
        return embarcacionRepository.findAll().stream()
                .map(embarcacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<EmbarcacionDTO> getAllPaged(Pageable pageable) {
        Page<EmbarcacionDTO> page = embarcacionRepository.findAll(pageable)
                .map(embarcacionMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public EmbarcacionDTO getById(Long id) {
        Embarcacion embarcacion = embarcacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Embarcación no encontrada con ID: " + id));
        return embarcacionMapper.toDTO(embarcacion);
    }

    @Transactional
    public EmbarcacionDTO create(CreateEmbarcacionRequest request) {
        Embarcacion embarcacion = embarcacionMapper.toEntity(request);
        Embarcacion saved = embarcacionRepository.save(embarcacion);
        return embarcacionMapper.toDTO(saved);
    }

    @Transactional
    public EmbarcacionDTO update(Long id, UpdateEmbarcacionRequest request) {
        Embarcacion existing = embarcacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Embarcación no encontrada con ID: " + id));

        embarcacionMapper.updateEntity(existing, request);
        Embarcacion updated = embarcacionRepository.save(existing);
        return embarcacionMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!embarcacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Embarcación no encontrada con ID: " + id);
        }
        embarcacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EmbarcacionDTO getByMatricula(String matricula) {
        Embarcacion embarcacion = embarcacionRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("Embarcación no encontrada con matrícula: " + matricula));
        return embarcacionMapper.toDTO(embarcacion);
    }

    @Transactional(readOnly = true)
    public List<EmbarcacionDTO> getByEmpresa(String empresa) {
        return embarcacionRepository.findByEmpresaPropietaria(empresa).stream()
                .map(embarcacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmbarcacionDTO> getByEstado(Embarcacion.EstadoEmbarcacion estado) {
        return embarcacionRepository.findByEstado(estado).stream()
                .map(embarcacionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
