package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Mantenimiento;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.MantenimientoRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mantenimientos")
@Tag(name = "Mantenimiento", description = "Endpont para gestionar la información de mantenieminto de un sistema")
public class MantenimientoController {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    // Obtener todos los mantenimientos
    @GetMapping
    public ResponseEntity<List<Mantenimiento>> getAllMantenimientos() {
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findAll();
        return new ResponseEntity<>(mantenimientos, HttpStatus.OK);
    }

    // Obtener un mantenimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mantenimiento> getMantenimientoById(@PathVariable Long id) {
        Optional<Mantenimiento> mantenimiento = mantenimientoRepository.findById(id);
        return mantenimiento.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo mantenimiento
    @PostMapping
    public ResponseEntity<Mantenimiento> createMantenimiento(@RequestBody Mantenimiento mantenimiento) {
        try {
            // Verificar que el sistema existe
            if (mantenimiento.getSistema() != null && mantenimiento.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(mantenimiento.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                mantenimiento.setSistema(sistema.get());
            }

            Mantenimiento newMantenimiento = mantenimientoRepository.save(mantenimiento);
            return new ResponseEntity<>(newMantenimiento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un mantenimiento existente
    @PutMapping("/{id}")
    public ResponseEntity<Mantenimiento> updateMantenimiento(@PathVariable Long id, @RequestBody Mantenimiento mantenimiento) {
        Optional<Mantenimiento> mantenimientoData = mantenimientoRepository.findById(id);

        if (mantenimientoData.isPresent()) {
            Mantenimiento updatedMantenimiento = mantenimientoData.get();

            // Verificar que el sistema existe
            if (mantenimiento.getSistema() != null && mantenimiento.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(mantenimiento.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedMantenimiento.setSistema(sistema.get());
            }

            updatedMantenimiento.setTipo(mantenimiento.getTipo());
            updatedMantenimiento.setFechaInicio(mantenimiento.getFechaInicio());
            updatedMantenimiento.setFechaFin(mantenimiento.getFechaFin());
            updatedMantenimiento.setEstado(mantenimiento.getEstado());
            updatedMantenimiento.setRealizadoPor(mantenimiento.getRealizadoPor());
            updatedMantenimiento.setDescripcion(mantenimiento.getDescripcion());
            updatedMantenimiento.setHallazgos(mantenimiento.getHallazgos());
            updatedMantenimiento.setRecomendaciones(mantenimiento.getRecomendaciones());
            updatedMantenimiento.setFechaProximoMantenimiento(mantenimiento.getFechaProximoMantenimiento());
            updatedMantenimiento.setCosto(mantenimiento.getCosto());
            updatedMantenimiento.setTiempoInactividad(mantenimiento.getTiempoInactividad());

            return new ResponseEntity<>(mantenimientoRepository.save(updatedMantenimiento), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un mantenimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMantenimiento(@PathVariable Long id) {
        try {
            mantenimientoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar mantenimientos por sistema
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<List<Mantenimiento>> getMantenimientosBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Mantenimiento> mantenimientos = mantenimientoRepository.findBySistema(sistema.get());
        return new ResponseEntity<>(mantenimientos, HttpStatus.OK);
    }

    // Buscar mantenimientos por sistema y estado
    @GetMapping("/buscar/sistema/{idSistema}/estado/{estado}")
    public ResponseEntity<List<Mantenimiento>> getMantenimientosBySistemaAndEstado(
            @PathVariable Long idSistema,
            @PathVariable Mantenimiento.EstadoMantenimiento estado) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Mantenimiento> mantenimientos = mantenimientoRepository.findBySistemaAndEstado(sistema.get(), estado);
        return new ResponseEntity<>(mantenimientos, HttpStatus.OK);
    }

    // Buscar mantenimientos por rango de fechas
    @GetMapping("/buscar/fechas")
    public ResponseEntity<List<Mantenimiento>> getMantenimientosByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findByFechaInicioAfterAndFechaInicioBefore(fechaInicio, fechaFin);
        return new ResponseEntity<>(mantenimientos, HttpStatus.OK);
    }

    // Buscar mantenimientos por tipo
    @GetMapping("/buscar/tipo/{tipo}")
    public ResponseEntity<List<Mantenimiento>> getMantenimientosByTipo(@PathVariable Mantenimiento.TipoMantenimiento tipo) {
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findByTipo(tipo);
        return new ResponseEntity<>(mantenimientos, HttpStatus.OK);
    }
}