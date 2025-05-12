package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.service.VersionSoftwareService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/versiones-software")
@Tag(name = "VersionSoftware", description = "Endpoints para gestionar las versiones de software de un sistema")
@RequiredArgsConstructor
@Slf4j
public class VersionSoftwareController {

    private final VersionSoftwareService versionSoftwareService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<VersionSoftwareDTO>>> getAllVersionesSoftware() {
        List<VersionSoftwareDTO> versiones = versionSoftwareService.getAllVersionesSoftware();
        return ResponseEntity.ok(ApiResponseDto.success(versiones));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<VersionSoftwareDTO>>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PaginatedResponse<VersionSoftwareDTO> response = versionSoftwareService.getAllVersionesSoftwarePaged(pageable);
        return ResponseEntity.ok(ApiResponseDto.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<VersionSoftwareDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDto.success(versionSoftwareService.getVersionSoftwareById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<VersionSoftwareDTO>> create(@RequestBody CreateVersionSoftwareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(versionSoftwareService.createVersionSoftware(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<VersionSoftwareDTO>> update(@PathVariable Long id,
                                                                     @RequestBody UpdateVersionSoftwareRequest request) {
        return ResponseEntity.ok(ApiResponseDto.success(versionSoftwareService.updateVersionSoftware(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        versionSoftwareService.deleteVersionSoftware(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<ApiResponseDto<List<VersionSoftwareDTO>>> getBySistema(@PathVariable Long idSistema) {
        return ResponseEntity.ok(ApiResponseDto.success(versionSoftwareService.getVersionesSoftwareBySistema(idSistema)));
    }

    @GetMapping("/buscar/sistema/{idSistema}/actual")
    public ResponseEntity<ApiResponseDto<VersionSoftwareDTO>> getActualBySistema(@PathVariable Long idSistema) {
        return ResponseEntity.ok(ApiResponseDto.success(versionSoftwareService.getVersionSoftwareActualBySistema(idSistema)));
    }

    @GetMapping("/buscar/fin-soporte")
    public ResponseEntity<ApiResponseDto<List<VersionSoftwareDTO>>> getByFinSoporte() {
        return ResponseEntity.ok(ApiResponseDto.success(versionSoftwareService.getVersionesSoftwarePorFinSoporte()));
    }
}
