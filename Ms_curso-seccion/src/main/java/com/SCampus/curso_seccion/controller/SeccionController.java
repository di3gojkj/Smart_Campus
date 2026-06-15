package com.SCampus.curso_seccion.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SCampus.curso_seccion.exception.ErrorResponseDTO;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.service.SeccionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seccion")
@RequiredArgsConstructor
@Tag(name = "Secciones", description = "Endpoints para la gestión, asignación y mantenimiento de secciones académicas")
public class SeccionController {

    private static final Logger logger = LoggerFactory.getLogger(SeccionController.class);
    private final SeccionService seccionService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las secciones", 
        description = "Retorna una lista completa con todas las secciones académicas configuradas en el campus."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de secciones recuperada exitosamente")
    })
    public ResponseEntity<List<Seccion>> obtenerSecciones() {
        logger.info("Petición HTTP GET recibida en /api/seccion");
        return ResponseEntity.ok(seccionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una sección por ID", 
        description = "Busca y devuelve los datos de una sección específica mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sección localizada exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "No se encontró ninguna sección con el ID proporcionado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<Seccion> obtenerSeccionPorId(
        @Parameter(description = "ID único de la sección a consultar", example = "5", required = true)
        @PathVariable Long id
    ) {
        logger.info("Petición HTTP GET recibida para recuperar Sección ID: {}", id);
        return seccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("La Sección ID {} no fue localizada en la BD", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @PostMapping
    @Operation(
        summary = "Crear una nueva sección", 
        description = "Registra una nueva sección académica en el sistema validando las restricciones del cuerpo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sección creada correctamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o faltantes (Error de Validación)",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<Seccion> crear(@Valid @RequestBody Seccion seccion) {
        logger.info("Petición HTTP POST recibida para dar de alta la sección: {}", seccion.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.guardar(seccion));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una sección existente", 
        description = "Reemplaza o actualiza por completo los datos de una sección académica identificada por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sección actualizada exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Los datos enviados para la actualización son inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<Seccion> actualizar(
        @Parameter(description = "ID único de la sección a modificar", example = "5", required = true)
        @PathVariable Long id, 
        @Valid @RequestBody Seccion seccion
    ) {
        logger.info("Petición HTTP PUT recibida para actualizar la sección ID: {}", id);
        return seccionService.obtenerPorId(id)
                .map(existente -> {
                    seccion.setId(id);
                    return ResponseEntity.ok(seccionService.guardar(seccion));
                })
                .orElseGet(() -> {
                    logger.warn("Intento fallido de actualización. Sección ID {} inexistente", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una sección académica", 
        description = "Remueve de forma permanente una sección de la base de datos a partir de su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "24", description = "Sección eliminada con éxito (Sin contenido de retorno)"),
        @ApiResponse(
            responseCode = "404", 
            description = "Operación cancelada. El ID de la sección no existe en el sistema",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID único de la sección a eliminar", example = "5", required = true)
        @PathVariable Long id
    ) {
        logger.info("Petición HTTP DELETE recibida para remover la sección ID: {}", id);
        if (seccionService.obtenerPorId(id).isEmpty()) {
            logger.warn("No se pudo ejecutar el borrado. Sección ID {} no existe", id);
            return ResponseEntity.notFound().build();
        }
        seccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}