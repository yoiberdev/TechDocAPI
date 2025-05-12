package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.TipoSistemaMapper;
import com.perucontrols.techdoc.model.TipoSistema;
import com.perucontrols.techdoc.repository.TipoSistemaRepository;
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
public class TipoSistemaService {

    private final TipoSistemaRepository tipoSistemaRepository;
    private final TipoSistemaMapper tipoSistemaMapper;

    @Transactional(readOnly = true)
    public List<TipoSistemaDTO> getAllTiposSistema() {
        return tipoSistemaRepository.findAll()
                .stream()
                .map(tipoSistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<TipoSistemaDTO> getAllTiposSistemaPaged(Pageable pageable) {
        Page<TipoSistemaDTO> page = tipoSistemaRepository.findAll(pageable)
                .map(tipoSistemaMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public TipoSistemaDTO getTipoSistemaById(Long id) {
        TipoSistema tipoSistema = tipoSistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de sistema no encontrado con ID: " + id));
        return tipoSistemaMapper.toDTO(tipoSistema);
    }

    @Transactional
    public TipoSistemaDTO createTipoSistema(CreateTipoSistemaRequest request) {
        TipoSistema tipoSistema = tipoSistemaMapper.toEntity(request);
        return tipoSistemaMapper.toDTO(tipoSistemaRepository.save(tipoSistema));
    }

    @Transactional
    public TipoSistemaDTO updateTipoSistema(Long id, UpdateTipoSistemaRequest request) {
        TipoSistema tipoSistema = tipoSistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de sistema no encontrado con ID: " + id));
        tipoSistemaMapper.updateEntity(tipoSistema, request);
        return tipoSistemaMapper.toDTO(tipoSistemaRepository.save(tipoSistema));
    }

    @Transactional
    public void deleteTipoSistema(Long id) {
        if (!tipoSistemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de sistema no encontrado con ID: " + id);
        }
        tipoSistemaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TipoSistemaDTO> getTiposSistemaByCategoria(TipoSistema.CategoriaSistema categoria) {
        return tipoSistemaRepository.findByCategoria(categoria)
                .stream()
                .map(tipoSistemaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TipoSistemaDTO> getTiposSistemaByNombre(String nombre) {
        return tipoSistemaRepository.findByNombreContaining(nombre)
                .stream()
                .map(tipoSistemaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
