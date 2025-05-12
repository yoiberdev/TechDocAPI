package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.Embarcacion;
import com.perucontrols.techdoc.service.EmbarcacionService;
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
@RequestMapping("/embarcaciones")
@Tag(name = "Embarcaciones", description = "Endpoints para gestionar embarcaciones")
@RequiredArgsConstructor
public class EmbarcacionController {

    private final EmbarcacionService embarcacionService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<EmbarcacionDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success("Lista obtenida", embarcacionService.getAll()));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<EmbarcacionDTO>>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(ApiResponseDto.success("Página obtenida", embarcacionService.getAllPaged(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EmbarcacionDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", embarcacionService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<EmbarcacionDTO>> create(@Valid @RequestBody CreateEmbarcacionRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Creado", embarcacionService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<EmbarcacionDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmbarcacionRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success("Actualizado", embarcacionService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        embarcacionService.delete(id);
        return ResponseEntity.ok(ApiResponseDto.success("Eliminado", null));
    }

    @GetMapping("/buscar/matricula/{matricula}")
    public ResponseEntity<ApiResponseDto<EmbarcacionDTO>> getByMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", embarcacionService.getByMatricula(matricula)));
    }

    @GetMapping("/buscar/empresa/{empresa}")
    public ResponseEntity<ApiResponseDto<List<EmbarcacionDTO>>> getByEmpresa(@PathVariable String empresa) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", embarcacionService.getByEmpresa(empresa)));
    }

    @GetMapping("/buscar/estado/{estado}")
    public ResponseEntity<ApiResponseDto<List<EmbarcacionDTO>>> getByEstado(
            @PathVariable Embarcacion.EstadoEmbarcacion estado) {
        return ResponseEntity.ok(ApiResponseDto.success("Encontrado", embarcacionService.getByEstado(estado)));
    }
}
