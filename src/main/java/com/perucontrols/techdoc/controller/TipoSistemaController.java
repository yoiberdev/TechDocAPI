package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.model.TipoSistema;
import com.perucontrols.techdoc.service.TipoSistemaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-sistema")
@Tag(name = "TipoSistema", description = "Endpoints para gestionar tipos de sistemas")
@RequiredArgsConstructor
public class TipoSistemaController {

    private final TipoSistemaService tipoSistemaService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<TipoSistemaDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.getAllTiposSistema()));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<TipoSistemaDTO>>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy);
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.getAllTiposSistemaPaged(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TipoSistemaDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.getTipoSistemaById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<TipoSistemaDTO>> create(@RequestBody CreateTipoSistemaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.createTipoSistema(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<TipoSistemaDTO>> update(@PathVariable Long id, @RequestBody UpdateTipoSistemaRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.updateTipoSistema(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        tipoSistemaService.deleteTipoSistema(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }

    @GetMapping("/buscar/categoria/{categoria}")
    public ResponseEntity<ApiResponseDto<List<TipoSistemaDTO>>> getByCategoria(@PathVariable TipoSistema.CategoriaSistema categoria) {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.getTiposSistemaByCategoria(categoria)));
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<ApiResponseDto<List<TipoSistemaDTO>>> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(ApiResponseDto.success(tipoSistemaService.getTiposSistemaByNombre(nombre)));
    }
}
