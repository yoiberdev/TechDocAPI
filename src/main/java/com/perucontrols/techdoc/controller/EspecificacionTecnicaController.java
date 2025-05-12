package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.service.EspecificacionTecnicaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especificaciones-tecnicas")
@Tag(name = "EspecificacionTecnica", description = "Endpoints para gestionar especificaciones técnicas")
@RequiredArgsConstructor
public class EspecificacionTecnicaController {

    private final EspecificacionTecnicaService service;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<EspecificacionTecnicaDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success("Lista obtenida", service.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EspecificacionTecnicaDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<EspecificacionTecnicaDTO>> create(
            @Valid @RequestBody CreateEspecificacionTecnicaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Creado", service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EspecificacionTecnicaDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEspecificacionTecnicaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Actualizado", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseDto.success("Eliminado", null));
    }

    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<ApiResponseDto<EspecificacionTecnicaDTO>> getBySistema(@PathVariable Long idSistema) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", service.getBySistema(idSistema)));
    }
}
