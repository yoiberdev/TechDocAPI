package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.model.Componente;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.ComponenteRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/componentes")
@Tag(name = "Componente", description = "Endpoints para gestionar los componentes de un sistema")
public class ComponenteController {

    @Autowired
    private ComponenteRepository componenteRepository;

    @Autowired
    private SistemaRepository sistemaRepository;

    // Obtener todos los componentes
    @GetMapping
    public ResponseEntity<List<Componente>> getAllComponentes() {
        List<Componente> componentes = componenteRepository.findAll();
        return new ResponseEntity<>(componentes, HttpStatus.OK);
    }

    // Obtener un componente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Componente> getComponenteById(@PathVariable Long id) {
        Optional<Componente> componente = componenteRepository.findById(id);
        return componente.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo componente
    @PostMapping
    public ResponseEntity<Componente> createComponente(@RequestBody Componente componente) {
        try {
            // Verificar que el sistema existe
            if (componente.getSistema() != null && componente.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(componente.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                componente.setSistema(sistema.get());
            }

            Componente newComponente = componenteRepository.save(componente);
            return new ResponseEntity<>(newComponente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un componente existente
    @PutMapping("/{id}")
    public ResponseEntity<Componente> updateComponente(@PathVariable Long id, @RequestBody Componente componente) {
        Optional<Componente> componenteData = componenteRepository.findById(id);

        if (componenteData.isPresent()) {
            Componente updatedComponente = componenteData.get();

            // Verificar que el sistema existe
            if (componente.getSistema() != null && componente.getSistema().getId() != null) {
                Optional<Sistema> sistema = sistemaRepository.findById(componente.getSistema().getId());
                if (sistema.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                updatedComponente.setSistema(sistema.get());
            }

            updatedComponente.setNombre(componente.getNombre());
            updatedComponente.setNumeroParte(componente.getNumeroParte());
            updatedComponente.setDescripcion(componente.getDescripcion());
            updatedComponente.setCantidad(componente.getCantidad());
            updatedComponente.setUbicacionEnSistema(componente.getUbicacionEnSistema());
            updatedComponente.setReemplazable(componente.getReemplazable());
            updatedComponente.setTiempoVidaUtil(componente.getTiempoVidaUtil());
            updatedComponente.setFechaInstalacion(componente.getFechaInstalacion());
            updatedComponente.setEstado(componente.getEstado());
            updatedComponente.setFabricante(componente.getFabricante());
            updatedComponente.setModelo(componente.getModelo());
            updatedComponente.setImagen(componente.getImagen());

            return new ResponseEntity<>(componenteRepository.save(updatedComponente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un componente
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteComponente(@PathVariable Long id) {
        try {
            componenteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar componentes por sistema
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<List<Componente>> getComponentesBySistema(@PathVariable Long idSistema) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Componente> componentes = componenteRepository.findBySistema(sistema.get());
        return new ResponseEntity<>(componentes, HttpStatus.OK);
    }

    // Buscar componentes por sistema y estado
    @GetMapping("/buscar/sistema/{idSistema}/estado/{estado}")
    public ResponseEntity<List<Componente>> getComponentesBySistemaAndEstado(
            @PathVariable Long idSistema,
            @PathVariable Componente.EstadoComponente estado) {
        Optional<Sistema> sistema = sistemaRepository.findById(idSistema);
        if (sistema.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Componente> componentes = componenteRepository.findBySistemaAndEstado(sistema.get(), estado);
        return new ResponseEntity<>(componentes, HttpStatus.OK);
    }

    // Buscar componentes por nombre
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Componente>> getComponentesByNombre(@PathVariable String nombre) {
        List<Componente> componentes = componenteRepository.findByNombreContaining(nombre);
        return new ResponseEntity<>(componentes, HttpStatus.OK);
    }
}