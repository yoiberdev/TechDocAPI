package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Mantenimiento;
import com.perucontrols.techdoc.service.MantenimientoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mantenimientos")
@Tag(name = "Mantenimiento", description = "Endpoints para gestionar mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<MantenimientoDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success("Lista obtenida", service.getAll()));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<MantenimientoDTO>>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponseDto.success("Página obtenida", service.getAllPaged(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<MantenimientoDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<MantenimientoDTO>> create(@Valid @RequestBody CreateMantenimientoRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Creado", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<MantenimientoDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMantenimientoRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Actualizado", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseDto.success("Eliminado", null));
    }

    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<ApiResponseDto<List<MantenimientoDTO>>> getBySistema(@PathVariable Long idSistema) {
        return ResponseEntity.ok(ApiResponseDto.success("Lista encontrada", service.getBySistema(idSistema)));
    }

    @GetMapping("/buscar/sistema/{idSistema}/estado/{estado}")
    public ResponseEntity<ApiResponseDto<List<MantenimientoDTO>>> getBySistemaAndEstado(
            @PathVariable Long idSistema, @PathVariable Mantenimiento.EstadoMantenimiento estado) {
        return ResponseEntity.ok(ApiResponseDto.success("Lista encontrada", service.getBySistemaAndEstado(idSistema, estado)));
    }

    @GetMapping("/buscar/fechas")
    public ResponseEntity<ApiResponseDto<List<MantenimientoDTO>>> getByRangoFechas(
            @RequestParam LocalDateTime fechaInicio, @RequestParam LocalDateTime fechaFin) {
        return ResponseEntity.ok(ApiResponseDto.success("Lista encontrada", service.getByRangoFechas(fechaInicio, fechaFin)));
    }

    @GetMapping("/buscar/tipo/{tipo}")
    public ResponseEntity<ApiResponseDto<List<MantenimientoDTO>>> getByTipo(@PathVariable Mantenimiento.TipoMantenimiento tipo) {
        return ResponseEntity.ok(ApiResponseDto.success("Lista encontrada", service.getByTipo(tipo)));
    }
}
