package com.smartCampus.Ms_Evaluacion.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.EvaluacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/evaluacion")

@Slf4j
@RequiredArgsConstructor
@Tag(
    name = "Evaluacion", 
    description = "Operaciones relacionadas con los tipos de evaluación"
)
public class EvaluacionController {

    private final EvaluacionService service;

    @Operation(
        summary = "Listar todas las evaluaciones",
        description = "Retorna un listado con todas las evaluaciones registradas en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EvaluacionResponseDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> listarTodas() {
        log.info("[EvaluacionController] GET - Solicitando listado de todas las evaluaciones");
        return ResponseEntity.ok(service.listarTodas()); 
    }

    @Operation(
        summary = "Buscar evaluaciones por su Tipo de Evaluación",
        description = "Retorna una lista de evaluaciones asociadas al ID del tipo de evaluación ingresado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista filtrada por tipo obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EvaluacionResponseDTO.class)
            )
        )
    })
    @GetMapping("/tipo/{idTipo}")
    public ResponseEntity<List<EvaluacionResponseDTO>> buscarPorTipo(@PathVariable Long idTipo) {
        log.info("[EvaluacionController] GET - Buscando evaluaciones por tipo ID: {}", idTipo);
        return ResponseEntity.ok(service.buscarPorTipo(idTipo));
    }

    @Operation(
        summary = "Buscar evaluaciones con filtros avanzados",
        description = "Filtra las evaluaciones por coincidencia parcial en el nombre y que tengan un porcentaje de ponderación mínimo"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Resultados de la búsqueda obtenidos correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EvaluacionResponseDTO.class)
            )
        )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<EvaluacionResponseDTO>> buscarPorNomberYPorcentaje(
            @RequestParam String nombre, 
            @RequestParam Double minPorcentaje) {
        log.info("[EvaluacionController] GET - Buscando evaluaciones filtradas por Nombre: '{}' y MinPorcentaje: {}", nombre, minPorcentaje);
        return ResponseEntity.ok(service.buscarPorNombreYPorcentaje(nombre, minPorcentaje));
    }

    @Operation(
        summary = "Crear una nueva evaluación académica",
        description = "Registra una evaluación en el sistema vinculándola a un Tipo de Evaluación existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Evaluación creada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EvaluacionResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o inconsistentes"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto: Ya existe una evaluación con ese nombre en el mismo tipo"
        )
    })
    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crear(@RequestBody EvaluacionRequestDTO dto) {
        log.info("[EvaluacionController] POST - Creando nueva evaluación: {}", dto.getNombre());
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualizar una evaluación existente",
        description = "Modifica los datos de una evaluación mediante su ID único"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Evaluación actualizada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = EvaluacionResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Evaluación o Tipo de Evaluación no encontrado"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto: El nombre ya está en uso por otra evaluación del mismo tipo"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody EvaluacionRequestDTO dto) {
        log.info("[EvaluacionController] PUT - Actualizando evaluación ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(
        summary = "Eliminar de forma permanente una evaluación",
        description = "Elimina el registro de la evaluación del sistema usando su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Evaluación eliminada correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se puede eliminar, ID inexistente"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[EvaluacionController] DELETE - Eliminando evaluación ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
