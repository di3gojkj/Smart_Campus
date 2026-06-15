package com.cur_eva.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.service.CursoEvaluacionService;

// ANOTACIONES DE SWAGGER
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estados")
@Tag(name = "Evaluaciones de Cursos", description = "Endpoints para la gestión de estados y evaluaciones de cursos")
public class CursoEvaluacionController {

    private final CursoEvaluacionService cursoEvaluacionService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las evaluaciones", 
        description = "Retorna una lista completa con todas las evaluaciones registradas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones recuperada exitosamente")
    })
    public ResponseEntity<List<CursoEvaluacionResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(cursoEvaluacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener evaluación por ID", 
        description = "Busca y retorna los detalles de una evaluación específica a través de su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró ninguna evaluación con el ID proporcionado")
    })
    public ResponseEntity<CursoEvaluacionResponseDTO> obtenerPorId(
        @Parameter(description = "ID único de la evaluación a buscar", example = "1", required = true)
        @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(cursoEvaluacionService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(
        summary = "Crear una nueva evaluación", 
        description = "Registra una nueva evaluación de curso en el sistema validando los campos obligatorios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evaluación creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Error de Validación)")
    })
    public ResponseEntity<CursoEvaluacionResponseDTO> crear(
        @Valid @RequestBody CursoEvaluacionRequestDTO dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoEvaluacionService.guardar(dto));
    }
}