package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.exception.OperacionNoPermitidaException;
import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.model.error.ErrorResponse;
import com.perucontrols.techdoc.repository.EmbarcacionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/embarcaciones")
@Tag(name = "Embarcaciones", description = "Endpoints para gestionar la información de embarcaciones")
public class EmbarcacionController {

    @Autowired
    private EmbarcacionRepository embarcacionRepository;

    @Operation(summary = "Obtener todas las embarcaciones", description = "Recupera una lista de todas las embarcaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAll();
        return new ResponseEntity<>(embarcaciones, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una embarcación por ID", description = "Recupera una embarcación específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Embarcación encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Embarcacion> getEmbarcacionById(
            @Parameter(description = "ID de la embarcación a buscar") @PathVariable Long id) {

        return embarcacionRepository.findById(id)
                .map(embarcacion -> new ResponseEntity<>(embarcacion, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la embarcación con ID: " + id));
    }

    @Operation(summary = "Crear una nueva embarcación", description = "Registra una nueva embarcación en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Embarcación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la embarcación inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Embarcacion> createEmbarcacion(
            @Parameter(description = "Embarcación a crear") @Valid @RequestBody Embarcacion embarcacion) {

        Optional<Embarcacion> existingEmbarcacion = embarcacionRepository.findByMatricula(embarcacion.getMatricula());
        if (existingEmbarcacion.isPresent()) {
            throw new DataIntegrityViolationException("Ya existe una embarcación con la matrícula: " + embarcacion.getMatricula());
        }

        Embarcacion newEmbarcacion = embarcacionRepository.save(embarcacion);
        return new ResponseEntity<>(newEmbarcacion, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una embarcación", description = "Actualiza los datos de una embarcación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Embarcación actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la embarcación inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Embarcacion> updateEmbarcacion(
            @Parameter(description = "ID de la embarcación a actualizar") @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la embarcación") @Valid @RequestBody Embarcacion embarcacion) {

        Embarcacion updatedEmbarcacion = embarcacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la embarcación con ID: " + id));

        // Si la matrícula cambia, verificar que no exista otra embarcación con esa matrícula
        if (!updatedEmbarcacion.getMatricula().equals(embarcacion.getMatricula())) {
            Optional<Embarcacion> existingEmbarcacion = embarcacionRepository.findByMatricula(embarcacion.getMatricula());
            if (existingEmbarcacion.isPresent() && !existingEmbarcacion.get().getId().equals(id)) {
                throw new DataIntegrityViolationException("Ya existe otra embarcación con la matrícula: " + embarcacion.getMatricula());
            }
        }

        // Actualizar datos
        updatedEmbarcacion.setNombre(embarcacion.getNombre());
        updatedEmbarcacion.setTipoEmbarcacion(embarcacion.getTipoEmbarcacion());
        updatedEmbarcacion.setMatricula(embarcacion.getMatricula());
        updatedEmbarcacion.setEmpresaPropietaria(embarcacion.getEmpresaPropietaria());
        updatedEmbarcacion.setCapacidadCarga(embarcacion.getCapacidadCarga());
        updatedEmbarcacion.setFechaConstruccion(embarcacion.getFechaConstruccion());
        updatedEmbarcacion.setEstado(embarcacion.getEstado());
        updatedEmbarcacion.setUbicacionActual(embarcacion.getUbicacionActual());
        updatedEmbarcacion.setNotas(embarcacion.getNotas());

        return new ResponseEntity<>(embarcacionRepository.save(updatedEmbarcacion), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una embarcación", description = "Elimina una embarcación del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Embarcación eliminada exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "No se puede eliminar la embarcación",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmbarcacion(
            @Parameter(description = "ID de la embarcación a eliminar") @PathVariable Long id) {

        // Verificar que la embarcación existe
        if (!embarcacionRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la embarcación con ID: " + id);
        }

        /* Ejemplo (comentado):
        if (relacionRepository.existsByEmbarcacionId(id)) {
            throw new OperacionNoPermitidaException("No se puede eliminar la embarcación porque está siendo utilizada en registros relacionados");
        }
        */

        embarcacionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Buscar embarcación por matrícula", description = "Recupera una embarcación específica por su matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Embarcación encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/buscar/matricula/{matricula}")
    public ResponseEntity<Embarcacion> getEmbarcacionByMatricula(
            @Parameter(description = "Matrícula de la embarcación a buscar") @PathVariable String matricula) {

        return embarcacionRepository.findByMatricula(matricula)
                .map(embarcacion -> new ResponseEntity<>(embarcacion, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ninguna embarcación con matrícula: " + matricula));
    }

    @Operation(summary = "Buscar embarcaciones por empresa propietaria", description = "Recupera todas las embarcaciones de una empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class)))
    })
    @GetMapping("/buscar/empresa/{empresa}")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByEmpresa(
            @Parameter(description = "Nombre de la empresa propietaria") @PathVariable String empresa) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findByEmpresaPropietaria(empresa);
        return new ResponseEntity<>(embarcaciones, HttpStatus.OK);
    }

    @Operation(summary = "Buscar embarcaciones por estado", description = "Recupera todas las embarcaciones que se encuentran en un estado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class)))
    })
    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByEstado(
            @Parameter(description = "Estado de la embarcación (ACTIVO, INACTIVO, MANTENIMIENTO)")
            @PathVariable Embarcacion.EstadoEmbarcacion estado) {
        List<Embarcacion> embarcaciones = embarcacionRepository.findByEstado(estado);
        return new ResponseEntity<>(embarcaciones, HttpStatus.OK);
    }
}