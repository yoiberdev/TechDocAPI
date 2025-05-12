package com.perucontrols.techdoc.service;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.mapper.DocumentacionMapper;
import com.perucontrols.techdoc.model.Documentacion;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.DocumentacionRepository;
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
public class DocumentacionService {

    private final DocumentacionRepository documentacionRepository;
    private final SistemaRepository sistemaRepository;
    private final DocumentacionMapper documentacionMapper;

    @Transactional(readOnly = true)
    public List<DocumentacionDTO> getAll() {
        return documentacionRepository.findAll().stream()
                .map(documentacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<DocumentacionDTO> getAllPaged(Pageable pageable) {
        Page<DocumentacionDTO> page = documentacionRepository.findAll(pageable)
                .map(documentacionMapper::toDTO);
        return PaginatedResponse.from(page);
    }

    @Transactional(readOnly = true)
    public DocumentacionDTO getById(Long id) {
        Documentacion doc = documentacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con ID: " + id));
        return documentacionMapper.toDTO(doc);
    }

    @Transactional
    public DocumentacionDTO create(CreateDocumentacionRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + request.getIdSistema()));

        Documentacion doc = documentacionMapper.toEntity(request);
        doc.setSistema(sistema);

        Documentacion saved = documentacionRepository.save(doc);
        return documentacionMapper.toDTO(saved);
    }

    @Transactional
    public DocumentacionDTO update(Long id, UpdateDocumentacionRequest request) {
        Documentacion existing = documentacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado con ID: " + id));

        if (request.getIdSistema() != null) {
            Sistema sistema = sistemaRepository.findById(request.getIdSistema())
                    .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + request.getIdSistema()));
            existing.setSistema(sistema);
        }

        documentacionMapper.updateEntity(existing, request);
        Documentacion updated = documentacionRepository.save(existing);
        return documentacionMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!documentacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Documento no encontrado con ID: " + id);
        }
        documentacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<DocumentacionDTO> getBySistema(Long idSistema) {
        Sistema sistema = sistemaRepository.findById(idSistema)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con ID: " + idSistema));

        return documentacionRepository.findBySistema(sistema).stream()
                .map(documentacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentacionDTO> getByTipo(Documentacion.TipoDocumento tipoDocumento) {
        return documentacionRepository.findByTipoDocumento(tipoDocumento).stream()
                .map(documentacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentacionDTO> getByTag(String tag) {
        return documentacionRepository.findByTagsContaining(tag).stream()
                .map(documentacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentacionDTO> getByTitulo(String titulo) {
        return documentacionRepository.findByTituloContaining(titulo).stream()
                .map(documentacionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
