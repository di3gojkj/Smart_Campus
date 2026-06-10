package com.smartCampus.Ms_Carrera.Controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraAsignaturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/carrera-asignatura")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Carrera-Asignatura",
    description = "Gestion de relaciones entre carreras y asignaturas"
)

public class CarreraAsignaturaController {
    private final CarreraAsignaturaService service;


    @Operation(
        summary = "Listar asignaturas por carrera",
        description = "Retorna todas las asignaturas asociadas a una carrera especifica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de carrera Asignatura retornada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CarreraAsignaturaResponseDTO.class)
            )
        )
    })
    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<CarreraAsignaturaResponseDTO>> listarTodas(
        @Parameter(
            description = "ID de la carrera asignatura a buscar",
            example = "1",
            required = true
        )
        @PathVariable Long idCarrera) {
        log.info("[CarreraAsignaturaController] GET (/api/carrera/{}", idCarrera);
        return ResponseEntity.ok(service.listarTodas(idCarrera));
    }

    @Operation(
        summary = "Crear nueva relacion",
        description = "Registra una nueva nueva relacion entre asignatura y carrera"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Relacion creada correctamente"
        ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada invalidos",
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                )
    })
    @PostMapping
    public ResponseEntity<CarreraAsignaturaResponseDTO> crear(@RequestBody CarreraAsignaturaRequestDTO dto) {
        log.info("[CarreraAsignaturaController] POST creando relación");
        CarreraAsignaturaResponseDTO creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
        summary = "actualiza una relacion existente",
        description = "actualiza una relacion existente entre asignatura y carrera"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Relacion actualizada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CarreraAsignaturaResponseDTO.class)
                )
        ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada invalidos",
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarreraAsignaturaResponseDTO> actualizar(@PathVariable Long id, 
        @Valid @RequestBody CarreraAsignaturaRequestDTO dto) {
        log.info("[CarreraAsignaturaController] PUT actualizando ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(
        summary = "elimina una relacion existente",
        description = "elimina una relacion existente entre asignatura y carrera"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Relacion eliminada correctamente"
        ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada invalidos"
                )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[CarreraAsignaturaController] DELETE eliminando ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
