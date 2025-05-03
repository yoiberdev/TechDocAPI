package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.repository.EmbarcacionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/embarcaciones")
@Tag(name = "Embarcaciones", description = "API para gestionar la información de embarcaciones")
public class EmbarcacionController {

    @Autowired
    private EmbarcacionRepository embarcacionRepository;

    @Operation(summary = "Obtener todas las embarcaciones", description = "Recupera una lista de todas las embarcaciones registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class)))
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
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Embarcacion> getEmbarcacionById(
            @Parameter(description = "ID de la embarcación a buscar") @PathVariable Long id) {
        Optional<Embarcacion> embarcacion = embarcacionRepository.findById(id);
        return embarcacion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear una nueva embarcación", description = "Registra una nueva embarcación en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Embarcación creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Embarcacion> createEmbarcacion(
            @Parameter(description = "Embarcación a crear") @RequestBody Embarcacion embarcacion) {
        try {
            Embarcacion newEmbarcacion = embarcacionRepository.save(embarcacion);
            return new ResponseEntity<>(newEmbarcacion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar una embarcación", description = "Actualiza los datos de una embarcación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Embarcación actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Embarcacion> updateEmbarcacion(
            @Parameter(description = "ID de la embarcación a actualizar") @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la embarcación") @RequestBody Embarcacion embarcacion) {
        Optional<Embarcacion> embarcacionData = embarcacionRepository.findById(id);

        if (embarcacionData.isPresent()) {
            Embarcacion updatedEmbarcacion = embarcacionData.get();
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
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una embarcación", description = "Elimina una embarcación del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Embarcación eliminada exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmbarcacion(
            @Parameter(description = "ID de la embarcación a eliminar") @PathVariable Long id) {
        try {
            embarcacionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Buscar embarcación por matrícula", description = "Recupera una embarcación específica por su matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Embarcación encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Embarcacion.class))),
            @ApiResponse(responseCode = "404", description = "Embarcación no encontrada",
                    content = @Content)
    })
    @GetMapping("/buscar/matricula/{matricula}")
    public ResponseEntity<Embarcacion> getEmbarcacionByMatricula(
            @Parameter(description = "Matrícula de la embarcación a buscar") @PathVariable String matricula) {
        Optional<Embarcacion> embarcacion = embarcacionRepository.findByMatricula(matricula);
        return embarcacion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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