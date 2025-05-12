package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Documentacion;
import com.perucontrols.techdoc.service.DocumentacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/documentacion")
@Tag(name = "Documentacion", description = "Endpoints para gestionar la documentación de un sistema")
@RequiredArgsConstructor
public class DocumentacionController {

    private final DocumentacionService documentacionService;

    @GetMapping
    @Operation(summary = "Obtener todos los documentos")
    public ResponseEntity<ApiResponseDto<List<DocumentacionDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getAll()));
    }

    @GetMapping("/paged")
    @Operation(summary = "Obtener documentos paginados")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<DocumentacionDTO>>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getAllPaged(pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un documento por ID")
    public ResponseEntity<ApiResponseDto<DocumentacionDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo documento")
    public ResponseEntity<ApiResponseDto<DocumentacionDTO>> create(@Valid @RequestBody CreateDocumentacionRequest request) {
        return ResponseEntity.status(201).body(ApiResponseDto.success(documentacionService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un documento existente")
    public ResponseEntity<ApiResponseDto<DocumentacionDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDocumentacionRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un documento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/sistema/{idSistema}")
    @Operation(summary = "Buscar documentos por sistema")
    public ResponseEntity<ApiResponseDto<List<DocumentacionDTO>>> getBySistema(@PathVariable Long idSistema) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getBySistema(idSistema)));
    }

    @GetMapping("/buscar/tipo/{tipoDocumento}")
    @Operation(summary = "Buscar documentos por tipo")
    public ResponseEntity<ApiResponseDto<List<DocumentacionDTO>>> getByTipo(@PathVariable Documentacion.TipoDocumento tipoDocumento) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getByTipo(tipoDocumento)));
    }

    @GetMapping("/buscar/tag/{tag}")
    @Operation(summary = "Buscar documentos por tag")
    public ResponseEntity<ApiResponseDto<List<DocumentacionDTO>>> getByTag(@PathVariable String tag) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getByTag(tag)));
    }

    @GetMapping("/buscar/titulo/{titulo}")
    @Operation(summary = "Buscar documentos por título")
    public ResponseEntity<ApiResponseDto<List<DocumentacionDTO>>> getByTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(ApiResponseDto.success(documentacionService.getByTitulo(titulo)));
    }
}
