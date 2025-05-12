package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.VersionSoftwareMapper;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.model.VersionSoftware;
import com.perucontrols.techdoc.repository.SistemaRepository;
import com.perucontrols.techdoc.repository.VersionSoftwareRepository;
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
public class VersionSoftwareService {

    private final VersionSoftwareRepository versionSoftwareRepository;
    private final SistemaRepository sistemaRepository;
    private final VersionSoftwareMapper versionSoftwareMapper;

    @Transactional(readOnly = true)
    public List<VersionSoftwareDTO> getAllVersionesSoftware() {
        return versionSoftwareRepository.findAll().stream()
                .map(versionSoftwareMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<VersionSoftwareDTO> getAllVersionesSoftwarePaged(Pageable pageable) {
        Page<VersionSoftwareDTO> page = versionSoftwareRepository.findAll(pageable)
                .map(versionSoftwareMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public VersionSoftwareDTO getVersionSoftwareById(Long id) {
        VersionSoftware version = versionSoftwareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versión de software no encontrada con ID: " + id));
        return versionSoftwareMapper.toDTO(version);
    }

    @Transactional
    public VersionSoftwareDTO createVersionSoftware(CreateVersionSoftwareRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + request.getIdSistema()));

        VersionSoftware version = versionSoftwareMapper.toEntity(request);
        version.setSistema(sistema);

        return versionSoftwareMapper.toDTO(versionSoftwareRepository.save(version));
    }

    @Transactional
    public VersionSoftwareDTO updateVersionSoftware(Long id, UpdateVersionSoftwareRequest request) {
        VersionSoftware version = versionSoftwareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versión de software no encontrada con ID: " + id));

        versionSoftwareMapper.updateEntity(version, request);
        return versionSoftwareMapper.toDTO(versionSoftwareRepository.save(version));
    }

    @Transactional
    public void deleteVersionSoftware(Long id) {
        if (!versionSoftwareRepository.existsById(id)) {
            throw new ResourceNotFoundException("Versión de software no encontrada con ID: " + id);
        }
        versionSoftwareRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<VersionSoftwareDTO> getVersionesSoftwareBySistema(Long idSistema) {
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + idSistema));

        return versionSoftwareRepository.findBySistema(sistema).stream()
                .map(versionSoftwareMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VersionSoftwareDTO getVersionSoftwareActualBySistema(Long idSistema) {
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + idSistema));

        VersionSoftware version = versionSoftwareRepository.findBySistemaAndEstado(sistema, VersionSoftware.EstadoVersion.ACTUAL)
                .orElseThrow(() -> new ResourceNotFoundException("Versión actual no encontrada para el sistema ID: " + idSistema));

        return versionSoftwareMapper.toDTO(version);
    }

    @Transactional(readOnly = true)
    public List<VersionSoftwareDTO> getVersionesSoftwarePorFinSoporte() {
        return versionSoftwareRepository.findByFechaFinSoporteLessThanEqual(LocalDate.now()).stream()
                .map(versionSoftwareMapper::toDTO)
                .collect(Collectors.toList());
    }
}
