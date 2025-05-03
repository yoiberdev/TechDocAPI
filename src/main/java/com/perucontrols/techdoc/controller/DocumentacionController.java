package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Documentacion;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.DocumentacionRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documentacion")
public class DocumentacionController {

    @Autowired
    private DocumentacionRepository documentacionRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    // Obtener toda la documentación
    @GetMapping
    public ResponseEntity<List<Documentacion>> getAllDocumentacion() {
        List<Documentacion> documentacion = documentacionRepository.findAll();
        return new ResponseEntity<>(documentacion, HttpStatus.OK);
    }

    // Obtener un documento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Documentacion> getDocumentacionById(@PathVariable Long id) {
        Optional<Documentacion> documentacion = documentacionRepository.findById(id);
        return documentacion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo documento
    @PostMapping
    public ResponseEntity<Documentacion> createDocumentacion(@RequestBody Documentacion documentacion) {
        try {
            // Verificar que el sistema existe
            if (documentacion.getSistema() != null && documentacion.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(documentacion.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                documentacion.setSistema(sistema.get());
            }

            Documentacion newDocumentacion = documentacionRepository.save(documentacion);
            return new ResponseEntity<>(newDocumentacion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un documento existente
    @PutMapping("/{id}")
    public ResponseEntity<Documentacion> updateDocumentacion(@PathVariable Long id, @RequestBody Documentacion documentacion) {
        Optional<Documentacion> documentacionData = documentacionRepository.findById(id);

        if (documentacionData.isPresent()) {
            Documentacion updatedDocumentacion = documentacionData.get();

            // Verificar que el sistema existe
            if (documentacion.getSistema() != null && documentacion.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(documentacion.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedDocumentacion.setSistema(sistema.get());
            }

            updatedDocumentacion.setTipoDocumento(documentacion.getTipoDocumento());
            updatedDocumentacion.setTitulo(documentacion.getTitulo());
            updatedDocumentacion.setArchivo(documentacion.getArchivo());
            updatedDocumentacion.setFechaCreacion(documentacion.getFechaCreacion());
            updatedDocumentacion.setCreadoPor(documentacion.getCreadoPor());
            updatedDocumentacion.setVersion(documentacion.getVersion());
            updatedDocumentacion.setDescripcion(documentacion.getDescripcion());
            updatedDocumentacion.setTags(documentacion.getTags());

            return new ResponseEntity<>(documentacionRepository.save(updatedDocumentacion), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un documento
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDocumentacion(@PathVariable Long id) {
        try {
            documentacionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar documentación por sistema
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<List<Documentacion>> getDocumentacionBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Documentacion> documentacion = documentacionRepository.findBySistema(sistema.get());
        return new ResponseEntity<>(documentacion, HttpStatus.OK);
    }

    // Buscar documentación por tipo de documento
    @GetMapping("/buscar/tipo/{tipoDocumento}")
    public ResponseEntity<List<Documentacion>> getDocumentacionByTipo(@PathVariable Documentacion.TipoDocumento tipoDocumento) {
        List<Documentacion> documentacion = documentacionRepository.findByTipoDocumento(tipoDocumento);
        return new ResponseEntity<>(documentacion, HttpStatus.OK);
    }

    // Buscar documentación por tag
    @GetMapping("/buscar/tag/{tag}")
    public ResponseEntity<List<Documentacion>> getDocumentacionByTag(@PathVariable String tag) {
        List<Documentacion> documentacion = documentacionRepository.findByTagContaining(tag);
        return new ResponseEntity<>(documentacion, HttpStatus.OK);
    }

    // Buscar documentación por título
    @GetMapping("/buscar/titulo/{titulo}")
    public ResponseEntity<List<Documentacion>> getDocumentacionByTitulo(@PathVariable String titulo) {
        List<Documentacion> documentacion = documentacionRepository.findByTituloContaining(titulo);
        return new ResponseEntity<>(documentacion, HttpStatus.OK);
    }
}