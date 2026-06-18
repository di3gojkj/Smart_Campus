package com.smartCampus.Ms_Evaluacion.controller;

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

import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.TipoEvaluacionService;

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


@RestController
@RequestMapping("/api/tipo-evaluacion")

@Slf4j
@RequiredArgsConstructor
@Tag(
    name = "TipoEvaluacion", 
    description = "Operaciones relacionadas con los tipos de evaluación"
)
public class TipoEvaluacionController {

    private final TipoEvaluacionService tipoEvaluacionService;

    @Operation(
        summary = "Listar todos los tipos de evaluacion",
        description = "Retorna una lista con todos los tipos de evaluacion registrados en la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de evaluacion retornada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TipoEvaluacionResponseDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<TipoEvaluacionResponseDTO>> listarTodos() {
        log.info("[TipoEvaluacionController] GET Listando todos los tipo evaluacion");
        return ResponseEntity.ok(tipoEvaluacionService.listarTodos());
    }

    
    @Operation(
        summary = "Busca un tipoEvaluacion por su ID",
        description = "Busca y retorna el tipoEvaluacion con el ID indicado."+
        "Puede retornar 404 en caso que no la encuentre en la tabla"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "tipoEvaluacion encontrada y retornada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TipoEvaluacionResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "tipoEvaluacion no encontrada en la BD",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoEvaluacionResponseDTO> buscarPorId(
        @Parameter(
            description = "ID de la Evaluacion a buscar",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ){
        log.info("[TipoEvaluacionController] GET Buscando tipo con ID: {}", id);
        return ResponseEntity.ok(tipoEvaluacionService.buscarPorId(id));
    }


    @Operation(
        summary = "Crea una nueva Evaluacion",
        description = "Registra una nueva Evaluacion en el sistema y valida que la sigla no se repita"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Evaluacion registrada exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PostMapping
    public ResponseEntity<TipoEvaluacionResponseDTO> crear(
        @Valid @RequestBody TipoEvaluacionRequestDTO dto) {
        log.info("[TipoEvaluacionController] POST Creando nuevo tipo: {}", dto.getNombreTipo());
        return new ResponseEntity<>(tipoEvaluacionService.crear(dto), HttpStatus.CREATED);
    }

    @Operation(
        summary = "Actualiza un TipoEvaluacion existente",
        description = "Modifica los datos de un TipoEvaluacion almacenada basandose en su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "TipoEvaluacion actualizada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = TipoEvaluacionResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoEvaluacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody TipoEvaluacionRequestDTO dto) {
        log.info("[TipoEvaluacionController] PUT Actualizando tipo ID: {}", id);
        return ResponseEntity.ok(tipoEvaluacionService.actualizar(id, dto));
    }


    @Operation(
        summary = "Elimina un TipoEvaluacion existente",
        description = "Elimina de forma permanente un TipoEvaluacion del sistema por su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "TipoEvaluacion eliminada correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada invalidos"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[TipoEvaluacionController] DELETE Eliminando tipo ID: {}", id);
        tipoEvaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
