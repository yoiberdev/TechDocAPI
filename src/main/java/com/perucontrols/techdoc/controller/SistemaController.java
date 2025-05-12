package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Sistema;
import com.perucontrols.techdoc.service.SistemaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sistemas")
@Tag(name = "Sistema", description = "Endpoints para gestionar los sistemas de embarcaciones")
@RequiredArgsConstructor
public class SistemaController {

    private final SistemaService sistemaService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getAllSistemas()));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<SistemaDTO>>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy);
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getAllSistemasPaged(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SistemaDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemaById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<SistemaDTO>> create(@Valid @RequestBody CreateSistemaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.createSistema(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SistemaDTO>> update(@PathVariable Long id, @RequestBody UpdateSistemaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.updateSistema(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        sistemaService.deleteSistema(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @GetMapping("/buscar/embarcacion/{idEmbarcacion}")
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getByEmbarcacion(@PathVariable Long idEmbarcacion) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemasByEmbarcacion(idEmbarcacion)));
    }

    @GetMapping("/buscar/tipo-sistema/{idTipoSistema}")
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getByTipoSistema(@PathVariable Long idTipoSistema) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemasByTipoSistema(idTipoSistema)));
    }

    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getByEstado(@PathVariable Sistema.EstadoSistema estado) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemasByEstado(estado)));
    }

    @GetMapping("/buscar/mantenimiento-requerido")
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getRequiringMaintenance() {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemasRequiringMaintenance()));
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<ApiResponseDto<List<SistemaDTO>>> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(ApiResponseDto.success(sistemaService.getSistemasByNombre(nombre)));
    }
}
