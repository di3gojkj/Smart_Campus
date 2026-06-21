package com.SCampus.curso_seccion.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.exception.ErrorResponseDTO;
import com.SCampus.curso_seccion.service.CursoService;

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
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Endpoints para la gestión y administración de cursos académicos")
public class CursoController {

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private final CursoService cursoService;

    @GetMapping
    @Operation(
        summary = "Obtener todos los cursos", 
        description = "Retorna una lista con la totalidad de los cursos registrados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cursos recuperada exitosamente")
    })
    public ResponseEntity<List<CursoResponseDTO>> obtenerTodos() {
        logger.info("Petición HTTP GET recibida en /api/cursos");
        return ResponseEntity.ok(cursoService.obtenerTodos());
    }

    @PostMapping("/guardar")
    @Operation(
        summary = "Registrar un nuevo curso", 
        description = "Crea un nuevo curso académico validando las restricciones del cuerpo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso creado de manera exitosa"),
        @ApiResponse(
            responseCode = "400", 
            description = "Fallo de validación. La fecha de creación es obligatoria o no cumple el formato DD/MM/AA",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Conflicto académico. El curso con esa fecha ya se encuentra registrado en el sistema",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<CursoResponseDTO> guardar(@Valid @RequestBody CursoRequestDTO cursoRequestDTO) {
        logger.info("Petición HTTP POST recibida en /api/cursos/guardar");
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardarCurso(cursoRequestDTO));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener un curso por su ID", 
        description = "Busca y devuelve el DTO de un curso específico mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso localizado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "No se encontró ningún curso con el ID proporcionado",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
        )
    })
    public ResponseEntity<CursoResponseDTO> obtenerPorId(
        @Parameter(description = "ID único del curso a consultar", example = "12", required = true)
        @PathVariable("id") Long id
    ) {
        logger.info("Petición HTTP GET recibida para recuperar Curso ID: {}", id);
        return cursoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("El Curso ID {} no fue localizado en la BD", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
