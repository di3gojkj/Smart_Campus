package com.smartcampus.msAsignatura.controller;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.service.AsignaturaService;

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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/asignaturas")

@Slf4j
@RequiredArgsConstructor
@Tag(
    name = "Asignatura Controller",
    description = "Controlador para la gestion de asignaturas academicas"
)
public class AsignaturaController {

    private final AsignaturaService asignaturaService;


    @Operation(
        summary = "Listar todas las asignaturas",
        description = "Retorna una lista con todas las asignaturas registradas en la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de asignaturas retornada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AsignaturaResponseDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<AsignaturaResponseDTO>> listarTodas() {
        log.info("[AsignaturaController] GET Listando todas las asignaturas");
        return ResponseEntity.ok(asignaturaService.listarTodas());
    }

 

    @Operation(
        summary = "Busca una asignatura por su ID",
        description = "Busca y retorna la Asignatura con el ID indicado."+
        "Puede retornar 404 en caso que no la encuentre en la tabla"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Asignatura encontrada y retornada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AsignaturaResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Asignatura no encontrada en la BD",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> buscarPorId(
        @Parameter(
            description = "ID de la asignatura a buscar",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ){
        log.info("[AsignaturaController] GET  /api/asignaturas/{}", id);
        return ResponseEntity.ok(asignaturaService.buscarPorId(id));
    }

    @Operation(
        summary = "Crea una nueva asignatura",
        description = "Registra una nueva asignatura en el sistema y valida que la sigla no se repita"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Asignatura registrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PostMapping
    public ResponseEntity<AsignaturaResponseDTO> crear(
        @Valid @RequestBody AsignaturaRequestDTO dto
    ){
        log.info("[AsignaturaController] POST Registrando nueva asignatura: {}", dto.getNombre());
        AsignaturaResponseDTO creado = asignaturaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
        summary = "Actualiza una asignatura existente",
        description = "Modifica los datos de una asignatura almacenada basandose en su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Asignatura actualizada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AsignaturaResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody AsignaturaRequestDTO dto) {
        log.info("[AsignaturaController] PUT Actualizando asignatura ID: {}", id);
        return ResponseEntity.ok(asignaturaService.actualizar(id, dto));
    }

    @Operation(
        summary = "Elimina una asignatura existente",
        description = "Elimina de forma permanente una asignatura del sistema por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Asignatura eliminada correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[AsignaturaController] DELETE Eliminando asignatura ID: {}", id);
        asignaturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
