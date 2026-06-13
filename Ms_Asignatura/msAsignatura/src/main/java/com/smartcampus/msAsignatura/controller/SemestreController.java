package com.smartcampus.msAsignatura.controller;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.service.SemestreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/semestres") 

@Slf4j
@RequiredArgsConstructor
@Tag(
    name = "Semestre Controller",
    description = "Controlador para la gestion de semestres academicos")
public class SemestreController {

    
    private final SemestreService semestreService;

    @Operation(
        summary = "Obtiene el listado de todos los semestres cronologicos",
        description = "Retorna una lista con todos los semestres ordenados cronologicamente de manera ascendente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de semestres obtenida correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SemestreResponseDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<SemestreResponseDTO>> listarTodos() {
        log.info("[SemestreController] GET Listando todos los semestres del sistema");
        return ResponseEntity.ok(semestreService.listarTodosCronologicos());
    }

    @Operation(
        summary = "Busca un semestre por su ID",
        description = "Retorna los detalles de un semestre especifico basado en su ID unico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Semestre encontrado con exito",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SemestreResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SemestreResponseDTO> buscarPorId(
        @Parameter(description = "ID del semestre a buscar", required = true, example = "1")
        @PathVariable Long id) {
        log.info("[SemestreController] GET Buscando semestre ID: {}", id);
        return ResponseEntity.ok(semestreService.buscarPorId(id));
    }

    @Operation(
        summary = "Crea un nuevo semestre",
        description = "Registra un nuevo periodo semestral validando que el nombre unico no se repita"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Semestre registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SemestreResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PostMapping
    public ResponseEntity<SemestreResponseDTO> crear(@Valid @RequestBody SemestreRequestDTO dto) {
        log.info("[SemestreController] POST Registrando nuevo semestre: {}", dto.getNombre());
        SemestreResponseDTO creado = semestreService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
        summary = "Actualiza un semestre existente",
        description = "Modifica los datos de un semestre almacenado basandose en su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Semestre actualizado correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = SemestreResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SemestreResponseDTO> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody SemestreRequestDTO dto) {
        log.info("[SemestreController] PUT Actualizando semestre ID: {}", id);
        return ResponseEntity.ok(semestreService.actualizar(id, dto));
    }

    @Operation(
        summary = "elimina una Carrera existente",
        description = "elimina una Carrera existente en la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Semestre eliminado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[SemestreController] DELETE Eliminando semestre ID: {}", id);
        semestreService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
