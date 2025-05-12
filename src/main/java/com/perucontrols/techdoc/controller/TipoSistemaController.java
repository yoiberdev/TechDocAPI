package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.exception.OperacionNoPermitidaException;
import com.perucontrols.techdoc.model.TipoSistema;
import com.perucontrols.techdoc.repository.TipoSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tipos-sistema")
@Tag(name = "Tipos de Sistema", description = "Endpoints para gestionar los tipos de sistemas disponibles")
public class TipoSistemaController {

    @Autowired
    private TipoSistemaRepository tipoSistemaRepository;

    @Operation(summary = "Obtener todos los tipos de sistema", description = "Recupera una lista de todos los tipos de sistema registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TipoSistema>> getAllTiposSistema() {
        List<TipoSistema> tiposSistema = tipoSistemaRepository.findAll();
        return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un tipo de sistema por ID", description = "Recupera un tipo de sistema específico según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de sistema encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de sistema no encontrado",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoSistema> getTipoSistemaById(
            @Parameter(description = "ID del tipo de sistema a buscar") @PathVariable Long id) {
        return tipoSistemaRepository.findById(id)
                .map(tipoSistema -> new ResponseEntity<>(tipoSistema, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el tipo de sistema con ID: " + id));
    }

    @Operation(summary = "Crear un nuevo tipo de sistema", description = "Registra un nuevo tipo de sistema en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de sistema creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos - El tipo de sistema ya existe",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<TipoSistema> createTipoSistema(@Valid @RequestBody TipoSistema tipoSistema) {
        try {
            // Verificar si ya existe un tipo de sistema con el mismo nombre
            if (tipoSistemaRepository.findByNombreContaining(tipoSistema.getNombre())
                    .stream().anyMatch(ts -> ts.getNombre().equalsIgnoreCase(tipoSistema.getNombre()))) {
                throw new DataIntegrityViolationException("Ya existe un tipo de sistema con el nombre: " + tipoSistema.getNombre());
            }

            TipoSistema newTipoSistema = tipoSistemaRepository.save(tipoSistema);
            return new ResponseEntity<>(newTipoSistema, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw e; // El GlobalExceptionHandler manejará esta excepción
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el tipo de sistema: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Actualizar un tipo de sistema existente", description = "Actualiza la información de un tipo de sistema registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de sistema actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo de sistema no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoSistema> updateTipoSistema(
            @Parameter(description = "ID del tipo de sistema a actualizar") @PathVariable Long id,
            @Valid @RequestBody TipoSistema tipoSistema) {

        // Verificar si el tipo de sistema existe
        TipoSistema existingTipoSistema = tipoSistemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el tipo de sistema con ID: " + id));

        try {
            // Verificar si el nuevo nombre ya existe en otro registro diferente al actual
            List<TipoSistema> similarNames = tipoSistemaRepository.findByNombreContaining(tipoSistema.getNombre());
            boolean duplicateNameExists = similarNames.stream()
                    .anyMatch(ts -> ts.getNombre().equalsIgnoreCase(tipoSistema.getNombre()) && !ts.getId().equals(id));

            if (duplicateNameExists) {
                throw new DataIntegrityViolationException("Ya existe otro tipo de sistema con el nombre: " + tipoSistema.getNombre());
            }

            // Actualizar los campos
            existingTipoSistema.setNombre(tipoSistema.getNombre());
            existingTipoSistema.setDescripcion(tipoSistema.getDescripcion());
            existingTipoSistema.setCategoria(tipoSistema.getCategoria());
            existingTipoSistema.setFabricanteRecomendado(tipoSistema.getFabricanteRecomendado());
            existingTipoSistema.setVidaUtilEstimada(tipoSistema.getVidaUtilEstimada());

            return new ResponseEntity<>(tipoSistemaRepository.save(existingTipoSistema), HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw e; // El GlobalExceptionHandler manejará esta excepción
        } catch (EntityNotFoundException e) {
            throw e; // El GlobalExceptionHandler manejará esta excepción
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el tipo de sistema: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Eliminar un tipo de sistema", description = "Elimina permanentemente un tipo de sistema del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de sistema eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Operación no permitida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo de sistema no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoSistema(
            @Parameter(description = "ID del tipo de sistema a eliminar") @PathVariable Long id) {

        // Verificar si el tipo de sistema existe
        if (!tipoSistemaRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el tipo de sistema con ID: " + id);
        }

        try {
            // Aquí podrías verificar si el tipo de sistema está siendo utilizado por otras entidades
            // Si es así, lanzar OperacionNoPermitidaException
            // Por ejemplo:
            /*
            if (sistemaRepository.existsByTipoSistemaId(id)) {
                throw new OperacionNoPermitidaException("No se puede eliminar el tipo de sistema porque está siendo utilizado por sistemas activos");
            }
            */

            tipoSistemaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OperacionNoPermitidaException e) {
            throw e; // El GlobalExceptionHandler manejará esta excepción
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el tipo de sistema: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Buscar tipos de sistema por categoría", description = "Recupera tipos de sistema filtrados por categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/buscar/categoria/{categoria}")
    public ResponseEntity<List<TipoSistema>> getTiposSistemaByCategoria(
            @Parameter(description = "Categoría del sistema") @PathVariable TipoSistema.CategoriaSistema categoria) {
        try {
            List<TipoSistema> tiposSistema = tipoSistemaRepository.findByCategoria(categoria);
            return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar tipos de sistema por categoría: " + e.getMessage(), e);
        }
    }

    @Operation(summary = "Buscar tipos de sistema por nombre", description = "Recupera tipos de sistema que contengan el nombre especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TipoSistema.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<TipoSistema>> getTiposSistemaByNombre(
            @Parameter(description = "Fragmento del nombre a buscar") @PathVariable String nombre) {
        try {
            List<TipoSistema> tiposSistema = tipoSistemaRepository.findByNombreContaining(nombre);
            return new ResponseEntity<>(tiposSistema, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar tipos de sistema por nombre: " + e.getMessage(), e);
        }
    }
}