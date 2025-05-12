package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.exception.BadRequestException;
import com.perucontrols.techdoc.exception.ResourceNotFoundException;
import com.perucontrols.techdoc.model.Componente;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.repository.ComponenteRepository;
import com.perucontrols.techdoc.repository.SistemaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/componentes")
@Tag(name = "Componente", description = "Endpoints para gestionar los componentes de un sistema")
@Validated
public class ComponenteController {

    private final ComponenteRepository componenteRepository;
    private final SistemaRepository sistemaRepository;

    @Autowired
    public ComponenteController(ComponenteRepository componenteRepository, SistemaRepository sistemaRepository) {
        this.componenteRepository = componenteRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @Operation(summary = "Obtener todos los componentes")
    @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida con éxito")
    @GetMapping
    public ResponseEntity<List<Componente>> getAllComponentes() {
        List<Componente> componentes = componenteRepository.findAll();
        return ResponseEntity.ok(componentes);
    }

    @Operation(summary = "Obtener un componente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Componente encontrado"),
            @ApiResponse(responseCode = "404", description = "Componente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Componente> getComponenteById(@PathVariable Long id) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Componente", "id", id));
        return ResponseEntity.ok(componente);
    }

    @Operation(summary = "Crear un nuevo componente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Componente creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Componente> createComponente(@Valid @RequestBody Componente componente) {
        // Verificar sistema usando método auxiliar
        if (componente.getSistema() != null && componente.getSistema().getId() != null) {
            Sistema sistema = getSistemaById(componente.getSistema().getId());
            componente.setSistema(sistema);
        }

        Componente newComponente = componenteRepository.save(componente);
        return new ResponseEntity<>(newComponente, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un componente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Componente actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Componente no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Componente> updateComponente(@PathVariable Long id, @Valid @RequestBody Componente componente) {
        // Verificar que el componente existe
        Componente updatedComponente = componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Componente", "id", id));

        // Verificar sistema usando método auxiliar
        if (componente.getSistema() != null && componente.getSistema().getId() != null) {
            Sistema sistema = getSistemaById(componente.getSistema().getId());
            updatedComponente.setSistema(sistema);
        }

        // Actualizar campos
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

        return ResponseEntity.ok(componenteRepository.save(updatedComponente));
    }

    @Operation(summary = "Eliminar un componente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Componente eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Componente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponente(@PathVariable Long id) {
        // Verificar que el componente existe
        if (!componenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Componente", "id", id);
        }

        componenteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar componentes por sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado")
    })
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<List<Componente>> getComponentesBySistema(@PathVariable Long idSistema) {
        Sistema sistema = getSistemaById(idSistema);
        List<Componente> componentes = componenteRepository.findBySistema(sistema);
        return ResponseEntity.ok(componentes);
    }

    @Operation(summary = "Buscar componentes por sistema y estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado")
    })
    @GetMapping("/buscar/sistema/{idSistema}/estado/{estado}")
    public ResponseEntity<List<Componente>> getComponentesBySistemaAndEstado(
            @PathVariable Long idSistema,
            @PathVariable Componente.EstadoComponente estado) {

        Sistema sistema = getSistemaById(idSistema);
        List<Componente> componentes = componenteRepository.findBySistemaAndEstado(sistema, estado);
        return ResponseEntity.ok(componentes);
    }

    @Operation(summary = "Buscar componentes por nombre")
    @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida con éxito")
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Componente>> getComponentesByNombre(@PathVariable String nombre) {
        List<Componente> componentes = componenteRepository.findByNombreContaining(nombre);
        return ResponseEntity.ok(componentes);
    }

    // Método auxiliar para obtener un sistema por ID o lanzar una excepción
    private Sistema getSistemaById(Long id) {
        return sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sistema", "id", id));
    }
}