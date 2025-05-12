package com.perucontrols.techdoc.controller;

import com.perucontrols.techdoc.dto.*;
import com.perucontrols.techdoc.service.ComponenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/componentes")
@Tag(name = "Componente", description = "Endpoints para gestionar los componentes de un sistema")
@RequiredArgsConstructor
@Slf4j
public class ComponenteController {

    private final ComponenteService componenteService;

    @Operation(summary = "Obtener todos los componentes", description = "Devuelve una lista de todos los componentes sin paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<ComponenteDTO>>> getAllComponentes() {
        try {
            List<ComponenteDTO> componentes = componenteService.getAllComponentes();
            return ResponseEntity.ok(ApiResponseDto.success(
                    String.format("Se encontraron %d componentes", componentes.size()),
                    componentes));
        } catch (Exception e) {
            log.error("Error al obtener componentes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("Error al obtener los componentes", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener componentes paginados", description = "Devuelve una lista paginada de componentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de componentes obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/paged")
    public ResponseEntity<ApiResponseDto<PaginatedResponse<ComponenteDTO>>> getComponentesPaged(
            @Parameter(description = "Número de página (0-indexado)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (ASC/DESC)") @RequestParam(defaultValue = "ASC") String sortDirection) {

        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            PaginatedResponse<ComponenteDTO> response = componenteService.getAllComponentesPaged(pageable);

            return ResponseEntity.ok(ApiResponseDto.success(
                    String.format("Página %d de %d", page + 1, response.getTotalPages()),
                    response));
        } catch (Exception e) {
            log.error("Error al obtener componentes paginados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("Error al obtener los componentes", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener un componente por ID", description = "Devuelve un componente específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Componente encontrado"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ComponenteDTO>> getComponenteById(
            @Parameter(description = "ID del componente") @PathVariable Long id) {
        try {
            ComponenteDTO componente = componenteService.getComponenteById(id);
            return ResponseEntity.ok(ApiResponseDto.success("Componente encontrado", componente));
        } catch (Exception e) {
            log.error("Error al obtener componente con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Componente no encontrado", e.getMessage()));
        }
    }

    @Operation(summary = "Crear un nuevo componente", description = "Crea un nuevo componente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Componente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Sistema no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<ComponenteDTO>> createComponente(
            @Valid @RequestBody CreateComponenteRequest request) {
        try {
            ComponenteDTO componente = componenteService.createComponente(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("Componente creado exitosamente", componente));
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear componente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al crear componente", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("Error al crear el componente", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un componente existente", description = "Actualiza los datos de un componente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Componente actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ComponenteDTO>> updateComponente(
            @Parameter(description = "ID del componente") @PathVariable Long id,
            @Valid @RequestBody UpdateComponenteRequest request) {
        try {
            ComponenteDTO componente = componenteService.updateComponente(id, request);
            return ResponseEntity.ok(ApiResponseDto.success("Componente actualizado exitosamente", componente));
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar componente {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar componente con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Error al actualizar el componente", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un componente", description = "Elimina un componente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Componente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteComponente(
            @Parameter(description = "ID del componente") @PathVariable Long id) {
        try {
            componenteService.deleteComponente(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Error al eliminar componente con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Error al eliminar el componente", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar componentes por sistema", description = "Devuelve todos los componentes de un sistema específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sistema no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/sistema/{idSistema}")
    public ResponseEntity<ApiResponseDto<List<ComponenteDTO>>> getComponentesBySistema(
            @Parameter(description = "ID del sistema") @PathVariable Long idSistema) {
        try {
            List<ComponenteDTO> componentes = componenteService.getComponentesBySistema(idSistema);
            return ResponseEntity.ok(ApiResponseDto.success(
                    String.format("Se encontraron %d componentes en el sistema", componentes.size()),
                    componentes));
        } catch (Exception e) {
            log.error("Error al buscar componentes del sistema: {}", idSistema, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Error al buscar componentes", e.getMessage()));
        }
    }

    @Operation(summary = "Buscar componentes por sistema y estado", description = "Devuelve los componentes de un sistema con un estado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Sistema no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/sistema/{idSistema}/estado/{estado}")
    public ResponseEntity<ApiResponseDto<List<ComponenteDTO>>> getComponentesBySistemaAndEstado(
            @Parameter(description = "ID del sistema") @PathVariable Long idSistema,
            @Parameter(description = "Estado del componente") @PathVariable String estado) {
        try {
            List<ComponenteDTO> componentes = componenteService.getComponentesBySistemaAndEstado(idSistema, estado);
            return ResponseEntity.ok(ApiResponseDto.success(
                    String.format("Se encontraron %d componentes con estado %s", componentes.size(), estado),
                    componentes));
        } catch (Exception e) {
            log.error("Error al buscar componentes del sistema {} con estado {}", idSistema, estado, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("Error al buscar componentes", e.getMessage()));
        }
    }
}
