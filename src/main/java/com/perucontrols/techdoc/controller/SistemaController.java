package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Embarcacion;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Obtener todos los sistemas
    @GetMapping
    public ResponseEntity<List<Sistema>> getAllSistemas() {
        List<Sistema> sistemas = sistemaRepository.findAll();
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    // Obtener un sistema por ID
    @GetMapping("/{id}")
    public ResponseEntity<Sistema> getSistemaById(@PathVariable Long id) {
        Optional<Sistema> sistema = sistemaRepository.findById(id);
        return sistema.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo sistema
    @PostMapping
    public ResponseEntity<Sistema> createSistema(@RequestBody Sistema sistema) {
        try {
            // Verificar que la embarcación existe
            if (sistema.getEmbarcacion() != null && sistema.getEmbarcacion().getId() != null) {
                Optional<Embarcacion> embarcacion = embarcacionRepository.findById(sistema.getEmbarcacion().getId());
                if (embarcacion.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                sistema.setEmbarcacion(embarcacion.get());
            }

            // Verificar que el tipo de sistema existe
            if (sistema.getTipoSistema() != null && sistema.getTipoSistema().getId() != null) {
                Optional<TipoSistema> tipoSistema = tipoSistemaRepository.findById(sistema.getTipoSistema().getId());
                if (tipoSistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                sistema.setTipoSistema(tipoSistema.get());
            }

            sistema.setCreatedAt(LocalDateTime.now());
            sistema.setUpdatedAt(LocalDateTime.now());

            Sistema newSistema = sistemaRepository.save(sistema);
            return new ResponseEntity<>(newSistema, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un sistema existente
    @PutMapping("/{id}")
    public ResponseEntity<Sistema> updateSistema(@PathVariable Long id, @RequestBody Sistema sistema) {
        Optional<Sistema> sistemaData = sistemaRepository.findById(id);

        if (sistemaData.isPresent()) {
            Sistema updatedSistema = sistemaData.get();

            // Verificar que la embarcación existe
            if (sistema.getEmbarcacion() != null && sistema.getEmbarcacion().getId() != null) {
                Optional<Embarcacion> embarcacion = embarcacionRepository.findById(sistema.getEmbarcacion().getId());
                if (embarcacion.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedSistema.setEmbarcacion(embarcacion.get());
            }

            // Verificar que el tipo de sistema existe
            if (sistema.getTipoSistema() != null && sistema.getTipoSistema().getId() != null) {
                Optional<TipoSistema> tipoSistema = tipoSistemaRepository.findById(sistema.getTipoSistema().getId());
                if (tipoSistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedSistema.setTipoSistema(tipoSistema.get());
            }

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
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un sistema
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteSistema(@PathVariable Long id) {
        try {
            sistemaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar sistemas por embarcación
    @GetMapping("/buscar/embarcacion/{idEmbarcacion}")
    public ResponseEntity<List<Sistema>> getSistemasByEmbarcacion(@PathVariable Long idEmbarcacion) {
        Optional<Embarcacion> embarcacion = embarcacionRepository.findById(idEmbarcacion);
        if (embarcacion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Sistema> sistemas = sistemaRepository.findByEmbarcacion(embarcacion.get());
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    // Buscar sistemas por tipo de sistema
    @GetMapping("/buscar/tipo-sistema/{idTipoSistema}")
    public ResponseEntity<List<Sistema>> getSistemasByTipoSistema(@PathVariable Long idTipoSistema) {
        Optional<TipoSistema> tipoSistema = tipoSistemaRepository.findById(idTipoSistema);
        if (tipoSistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Sistema> sistemas = sistemaRepository.findByTipoSistema(tipoSistema.get());
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    // Buscar sistemas por estado
    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<List<Sistema>> getSistemasByEstado(@PathVariable Sistema.EstadoSistema estado) {
        List<Sistema> sistemas = sistemaRepository.findByEstado(estado);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    // Buscar sistemas que requieren mantenimiento
    @GetMapping("/buscar/mantenimiento-requerido")
    public ResponseEntity<List<Sistema>> getSistemasRequiringMaintenance() {
        LocalDate fechaActual = LocalDate.now();
        List<Sistema> sistemas = sistemaRepository.findSistemasRequiringMaintenance(fechaActual);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }

    // Buscar sistemas por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Sistema>> getSistemasByNombre(@PathVariable String nombre) {
        List<Sistema> sistemas = sistemaRepository.findByNombreContaining(nombre);
        return new ResponseEntity<>(sistemas, HttpStatus.OK);
    }
}