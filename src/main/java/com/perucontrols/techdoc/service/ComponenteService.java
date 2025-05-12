package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Componente;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.ComponenteMapper;
import com.perucontrols.techdoc.repository.ComponenteRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
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
public class ComponenteService {

    private final ComponenteRepository componenteRepository;
    private final SistemaRepository sistemaRepository;
    private final ComponenteMapper componenteMapper;

    @Transactional(readOnly = true)
    public List<ComponenteDTO> getAllComponentes() {
        log.info("Obteniendo todos los componentes");
        return componenteRepository.findAll().stream()
                .map(componenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ComponenteDTO> getAllComponentesPaged(Pageable pageable) {
        log.info("Obteniendo componentes paginados: página {}, tamaño {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        Page<ComponenteDTO> page = componenteRepository.findAll(pageable)
                .map(componenteMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public ComponenteDTO getComponenteById(Long id) {
        log.info("Buscando componente con ID: {}", id);
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Componente no encontrado con ID: %d", id)));
        return componenteMapper.toDTO(componente);
    }

    @Transactional
    public ComponenteDTO createComponente(CreateComponenteRequest request) {
        log.info("Creando nuevo componente: {}", request.getNombre());
        
        // Validar que el sistema existe
        Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Sistema no encontrado con ID: %d", request.getIdSistema())));
        
        Componente componente = componenteMapper.toEntity(request);
        componente.setSistema(sistema);
        
        // Validar estado
        if (request.getEstado() != null) {
            try {
                Componente.EstadoComponente estado = 
                    Componente.EstadoComponente.valueOf(request.getEstado().toUpperCase());
                componente.setEstado(estado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    String.format("Estado inválido: %s. Estados válidos: %s", 
                        request.getEstado(), 
                        java.util.Arrays.toString(Componente.EstadoComponente.values())));
            }
        }
        
        Componente savedComponente = componenteRepository.save(componente);
        log.info("Componente creado exitosamente con ID: {}", savedComponente.getId());
        return componenteMapper.toDTO(savedComponente);
    }

    @Transactional
    public ComponenteDTO updateComponente(Long id, UpdateComponenteRequest request) {
        log.info("Actualizando componente con ID: {}", id);
        
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Componente no encontrado con ID: %d", id)));
        
        // Si se cambia el sistema, validar que existe
        if (request.getIdSistema() != null && 
            !request.getIdSistema().equals(componente.getSistema().getId())) {
            Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Sistema no encontrado con ID: %d", request.getIdSistema())));
            componente.setSistema(sistema);
        }
        
        // Actualizar campos
        componenteMapper.updateEntity(componente, request);
        
        // Validar estado si se proporciona
        if (request.getEstado() != null) {
            try {
                Componente.EstadoComponente estado = 
                    Componente.EstadoComponente.valueOf(request.getEstado().toUpperCase());
                componente.setEstado(estado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    String.format("Estado inválido: %s. Estados válidos: %s", 
                        request.getEstado(), 
                        java.util.Arrays.toString(Componente.EstadoComponente.values())));
            }
        }
        
        Componente updatedComponente = componenteRepository.save(componente);
        log.info("Componente actualizado exitosamente: ID {}", id);
        return componenteMapper.toDTO(updatedComponente);
    }

    @Transactional
    public void deleteComponente(Long id) {
        log.info("Eliminando componente con ID: {}", id);
        
        if (!componenteRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                String.format("Componente no encontrado con ID: %d", id));
        }
        
        componenteRepository.deleteById(id);
        log.info("Componente eliminado exitosamente: ID {}", id);
    }

    @Transactional(readOnly = true)
    public List<ComponenteDTO> getComponentesBySistema(Long idSistema) {
        log.info("Buscando componentes del sistema con ID: {}", idSistema);
        
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Sistema no encontrado con ID: %d", idSistema)));
        
        return componenteRepository.findBySistema(sistema).stream()
                .map(componenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComponenteDTO> getComponentesBySistemaAndEstado(Long idSistema, String estadoStr) {
        log.info("Buscando componentes del sistema {} con estado {}", idSistema, estadoStr);
        
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Sistema no encontrado con ID: %d", idSistema)));
        
        Componente.EstadoComponente estado;
        try {
            estado = Componente.EstadoComponente.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Estado inválido: %s. Estados válidos: %s", 
                    estadoStr, 
                    java.util.Arrays.toString(Componente.EstadoComponente.values())));
        }
        
        return componenteRepository.findBySistemaAndEstado(sistema, estado).stream()
                .map(componenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComponenteDTO> getComponentesByNombre(String nombre) {
        log.info("Buscando componentes con nombre que contiene: {}", nombre);
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        
        return componenteRepository.findByNombreContaining(nombre).stream()
                .map(componenteMapper::toDTO)
                .collect(Collectors.toList());
    }
}