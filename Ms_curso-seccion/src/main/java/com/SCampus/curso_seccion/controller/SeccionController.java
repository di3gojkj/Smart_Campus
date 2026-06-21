package com.SCampus.curso_seccion.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SCampus.curso_seccion.dto.SeccionResponseDTO;
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
@Tag(name = "Secciones", description = "Endpoints para la gestión, mantenimiento y enriquecimiento de secciones académicas")
public class SeccionController {

    private static final Logger logger = LoggerFactory.getLogger(SeccionController.class);
    private final SeccionService seccionService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las secciones enriquecidas", 
        description = "Retorna una lista completa con todas las secciones académicas e inyecta dinámicamente los metadatos desde el Ms de Carreras."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de secciones recuperada y enriquecida exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SeccionResponseDTO.class))
        )
    })
    public ResponseEntity<List<SeccionResponseDTO>> obtenerSecciones() {
        logger.info("Petición HTTP GET recibida en /api/seccion para recuperar listado enriquecido");
        return ResponseEntity.ok(seccionService.obtenerTodasEnriquecidas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una sección enriquecida por ID", 
        description = "Busca y devuelve los datos consolidados de una sección específica mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sección localizada y enriquecida exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "No se encontró ninguna sección con el ID proporcionado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<SeccionResponseDTO> obtenerSeccionPorId(
        @Parameter(description = "ID único de la sección a consultar", example = "5", required = true)
        @PathVariable Long id
    ) {
        logger.info("Petición HTTP GET recibida para recuperar Sección ID consolidada: {}", id);
        return seccionService.obtenerPorIdEnriquecido(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("La Sección ID {} no fue localizada en la BD", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @PostMapping
    @Operation(
        summary = "Crear una nueva sección con validación distribuida", 
        description = "Registra una sección en el sistema local tras validar sincrónicamente por Feign que la relación de carrera académica sea íntegra."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sección validada y creada correctamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o error de integridad referencial distribuida",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<SeccionResponseDTO> crear(
        @Valid @RequestBody Seccion seccion,
        @Parameter(description = "ID de la carrera remota con el que se debe validar la integridad en Ms_Carrera", example = "1", required = true)
        @RequestParam(value = "idCarreraVerificar", defaultValue = "1") Long idCarreraVerificar
    ) {
        logger.info("Petición HTTP POST recibida para dar de alta la sección: {} con validación perimetral para Carrera: {}", seccion.getNombre(), idCarreraVerificar);
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.guardarEnriquecido(seccion, idCarreraVerificar));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una sección académica", 
        description = "Remueve de forma permanente una sección de la base de datos a partir de su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sección eliminada con éxito (Sin contenido de retorno)"),
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
        if (seccionService.obtenerPorIdEnriquecido(id).isEmpty()) {
            logger.warn("No se pudo ejecutar el borrado. Sección ID {} no existe", id);
            return ResponseEntity.notFound().build();
        }
        seccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
