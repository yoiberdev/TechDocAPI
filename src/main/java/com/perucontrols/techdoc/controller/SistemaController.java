package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.exception.OperacionNoPermitidaException;
import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.model.error.ErrorResponse;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.model.TipoSistema;
import com.perucontrols.techdoc.repository.EmbarcacionRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import com.perucontrols.techdoc.repository.TipoSistemaRepository;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sistemas")
@Tag(name = "Sistemas", description = "Endpoints para gestionar la información de sistemas instalados en embarcaciones")
public class SistemaController {

    @Autowired
    private SistemaRepository sistemaRepository;

    @Autowired
    private EmbarcacionRepository embarcacionRepository;

    @Autowired
    private TipoSistemaRepository tipoSistemaRepository;

    @Operation(summary = "Obtener todos los sistemas", description = "Recupera una lista de todos los sistemas registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Sistema>> getAllSistemas() {
        List<Sistema> sistemas = sistemaRepository.findAll();
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un sistema por ID", description = "Recupera un sistema específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sistema encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sistema> getSistemaById(
            @Parameter(description = "ID del sistema a buscar") @PathVariable Long id) {

        return sistemaRepository.findById(id)
                .map(sistema -> new ResponseEntity<>(sistema, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el sistema con ID: " + id));
    }

    @Operation(summary = "Crear un nuevo sistema", description = "Registra un nuevo sistema en la embarcación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sistema creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "400", description = "Datos del sistema inválidos o referencias no encontradas",
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
    public ResponseEntity<Sistema> createSistema(
            @Parameter(description = "Sistema a crear") @Valid @RequestBody Sistema sistema) {

        // Verificar que la embarcación existe
        if (sistema.getEmbarcacion() != null && sistema.getEmbarcacion().getId() != null) {
            Embarcacion embarcacion = embarcacionRepository.findById(sistema.getEmbarcacion().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No se encontró la embarcación con ID: " + sistema.getEmbarcacion().getId()));
            sistema.setEmbarcacion(embarcacion);
        } else {
            throw new IllegalArgumentException("Se requiere especificar una embarcación válida");
        }

        // Verificar que el tipo de sistema existe
        if (sistema.getTipoSistema() != null && sistema.getTipoSistema().getId() != null) {
            TipoSistema tipoSistema = tipoSistemaRepository.findById(sistema.getTipoSistema().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No se encontró el tipo de sistema con ID: " + sistema.getTipoSistema().getId()));
            sistema.setTipoSistema(tipoSistema);
        } else {
            throw new IllegalArgumentException("Se requiere especificar un tipo de sistema válido");
        }

        // Verificar si ya existe un sistema con el mismo número de serie en la misma embarcación
        if (sistema.getNumeroSerie() != null && !sistema.getNumeroSerie().isEmpty()) {
            Optional<Sistema> existingSistema = sistemaRepository.findByNumeroSerieAndEmbarcacion(
                    sistema.getNumeroSerie(), sistema.getEmbarcacion());
            if (existingSistema.isPresent()) {
                throw new DataIntegrityViolationException(
                        "Ya existe un sistema con el número de serie '" + sistema.getNumeroSerie() +
                                "' en la embarcación ID: " + sistema.getEmbarcacion().getId());
            }
        }

        // Establecer fechas de creación y actualización
        sistema.setCreatedAt(LocalDateTime.now());
        sistema.setUpdatedAt(LocalDateTime.now());

        Sistema newSistema = sistemaRepository.save(sistema);
        return new ResponseEntity<>(newSistema, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un sistema", description = "Actualiza los datos de un sistema existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sistema actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "400", description = "Datos del sistema inválidos o referencias no encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sistema> updateSistema(
            @Parameter(description = "ID del sistema a actualizar") @PathVariable Long id,
            @Parameter(description = "Nuevos datos del sistema") @Valid @RequestBody Sistema sistema) {

        // Verificar que el sistema existe
        Sistema updatedSistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el sistema con ID: " + id));

        // Verificar que la embarcación existe si se proporciona
        if (sistema.getEmbarcacion() != null && sistema.getEmbarcacion().getId() != null) {
            Embarcacion embarcacion = embarcacionRepository.findById(sistema.getEmbarcacion().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No se encontró la embarcación con ID: " + sistema.getEmbarcacion().getId()));
            updatedSistema.setEmbarcacion(embarcacion);
        }

        // Verificar que el tipo de sistema existe si se proporciona
        if (sistema.getTipoSistema() != null && sistema.getTipoSistema().getId() != null) {
            TipoSistema tipoSistema = tipoSistemaRepository.findById(sistema.getTipoSistema().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No se encontró el tipo de sistema con ID: " + sistema.getTipoSistema().getId()));
            updatedSistema.setTipoSistema(tipoSistema);
        }

        // Verificar si hay conflicto con el número de serie
        if (sistema.getNumeroSerie() != null && !sistema.getNumeroSerie().equals(updatedSistema.getNumeroSerie())) {
            Optional<Sistema> existingSistema = sistemaRepository.findByNumeroSerieAndEmbarcacion(
                    sistema.getNumeroSerie(), updatedSistema.getEmbarcacion());
            if (existingSistema.isPresent() && !existingSistema.get().getId().equals(id)) {
                throw new DataIntegrityViolationException(
                        "Ya existe otro sistema con el número de serie '" + sistema.getNumeroSerie() +
                                "' en la misma embarcación");
            }
        }

        // Actualizar datos del sistema
        updatedSistema.setNombre(sistema.getNombre());
        updatedSistema.setNumeroSerie(sistema.getNumeroSerie());
        updatedSistema.setFechaInstalacion(sistema.getFechaInstalacion());
        updatedSistema.setUbicacionEnEmbarcacion(sistema.getUbicacionEnEmbarcacion());
        updatedSistema.setEstado(sistema.getEstado());
        updatedSistema.setFechaUltimaRevision(sistema.getFechaUltimaRevision());
        updatedSistema.setFechaProximoMantenimiento(sistema.getFechaProximoMantenimiento());
        updatedSistema.setTiempoVidaRestante(sistema.getTiempoVidaRestante());
        updatedSistema.setTecnicoInstalador(sistema.getTecnicoInstalador());
        updatedSistema.setNotasInstalacion(sistema.getNotasInstalacion());
        updatedSistema.setDiagramaUbicacion(sistema.getDiagramaUbicacion());
        updatedSistema.setUpdatedAt(LocalDateTime.now());

        return new ResponseEntity<>(sistemaRepository.save(updatedSistema), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un sistema", description = "Elimina un sistema del registro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sistema eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "No se puede eliminar el sistema",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSistema(
            @Parameter(description = "ID del sistema a eliminar") @PathVariable Long id) {

        // Verificar que el sistema existe
        if (!sistemaRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el sistema con ID: " + id);
        }

        // Aquí podrías añadir verificaciones adicionales antes de eliminar
        // Por ejemplo, verificar si el sistema tiene componentes asociados

        /* Ejemplo (comentado):
        if (componenteRepository.existsBySistemaId(id)) {
            throw new OperacionNoPermitidaException("No se puede eliminar el sistema porque tiene componentes asociados");
        }
        */

        sistemaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Buscar sistemas por embarcación", description = "Recupera todos los sistemas instalados en una embarcación específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/buscar/embarcacion/{idEmbarcacion}")
    public ResponseEntity<List<Sistema>> getSistemasByEmbarcacion(
            @Parameter(description = "ID de la embarcación") @PathVariable Long idEmbarcacion) {

        Embarcacion embarcacion = embarcacionRepository.findById(idEmbarcacion)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la embarcación con ID: " + idEmbarcacion));

        List<Sistema> sistemas = sistemaRepository.findByEmbarcacion(embarcacion);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar sistemas por tipo", description = "Recupera todos los sistemas de un tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de sistema no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/buscar/tipo-sistema/{idTipoSistema}")
    public ResponseEntity<List<Sistema>> getSistemasByTipoSistema(
            @Parameter(description = "ID del tipo de sistema") @PathVariable Long idTipoSistema) {

        TipoSistema tipoSistema = tipoSistemaRepository.findById(idTipoSistema)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el tipo de sistema con ID: " + idTipoSistema));

        List<Sistema> sistemas = sistemaRepository.findByTipoSistema(tipoSistema);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar sistemas por estado", description = "Recupera todos los sistemas que se encuentran en un estado específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class)))
    })
    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<List<Sistema>> getSistemasByEstado(
            @Parameter(description = "Estado del sistema (OPERATIVO, MANTENIMIENTO, REPARACION, INACTIVO)")
            @PathVariable Sistema.EstadoSistema estado) {

        List<Sistema> sistemas = sistemaRepository.findByEstado(estado);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar sistemas que requieren mantenimiento", description = "Recupera todos los sistemas cuya fecha de próximo mantenimiento es anterior o igual a la fecha actual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class)))
    })
    @GetMapping("/buscar/mantenimiento-requerido")
    public ResponseEntity<List<Sistema>> getSistemasRequiringMaintenance() {
        LocalDate fechaActual = LocalDate.now();
        List<Sistema> sistemas = sistemaRepository.findSistemasRequiringMaintenance(fechaActual);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    @Operation(summary = "Buscar sistemas por nombre", description = "Recupera todos los sistemas cuyo nombre contiene el texto especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sistema.class)))
    })
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Sistema>> getSistemasByNombre(
            @Parameter(description = "Texto a buscar en el nombre") @PathVariable String nombre) {

        List<Sistema> sistemas = sistemaRepository.findByNombreContaining(nombre);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }
}