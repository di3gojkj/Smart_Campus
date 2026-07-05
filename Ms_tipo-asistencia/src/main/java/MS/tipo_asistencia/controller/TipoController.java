package MS.tipo_asistencia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.service.TipoService;


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
@RequestMapping("/api/tipo")
@RequiredArgsConstructor
@Tag(name = "Catálogo de Tipos", description = "Endpoints para administrar el catálogo de tipos de asistencia (Presente, Ausente, Justificado, etc.)")
public class TipoController {

    private final TipoService tipoService;

    @GetMapping
    @Operation(
        summary = "Listar todos los tipos de asistencia", 
        description = "Retorna el catálogo completo con las diferentes clasificaciones de asistencia configuradas en la institución."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catálogo recuperado exitosamente")
    })
    public ResponseEntity<List<TipoResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(tipoService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener tipo de asistencia por ID", 
        description = "Busca y devuelve una clasificación específica del catálogo mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de asistencia localizado"),
        @ApiResponse(
            responseCode = "404", 
            description = "El ID de tipo proporcionado no existe en el catálogo",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<TipoResponseDTO> obtenerPorId(
        @Parameter(description = "ID único del tipo de asistencia a consultar", example = "1", required = true)
        @PathVariable Long id
    ) {
        TipoResponseDTO response = tipoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
        summary = "Crear una nueva clasificación de asistencia", 
        description = "Registra una nueva opción en el catálogo (por ejemplo: 'Atrasado Justificado') validando los datos obligatorios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Nueva clasificación creada correctamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "El cuerpo de la petición contiene errores de validación",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<TipoResponseDTO> crear(@Valid @RequestBody TipoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una clasificación existente", 
        description = "Modifica las propiedades de una categoría del catálogo identificada por su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clasificación actualizada con éxito"),
        @ApiResponse(
            responseCode = "400", 
            description = "Los datos provistos en el Request son inválidos",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No se localizó la categoría con el ID suministrado",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<TipoResponseDTO> actualizar(
        @Parameter(description = "ID único del tipo de asistencia a modificar", example = "1", required = true)
        @PathVariable Long id, 
        @Valid @RequestBody TipoRequestDTO dto
    ) {
        TipoResponseDTO response = tipoService.actualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover una clasificación del catálogo", 
        description = "Elimina permanentemente una categoría de asistencia de la base de datos a partir de su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "24", description = "Categoría eliminada de manera exitosa"),
        @ApiResponse(
            responseCode = "404", 
            description = "El ID especificado no pertenece a ninguna opción del catálogo",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID único del tipo de asistencia a eliminar", example = "1", required = true)
        @PathVariable Long id
    ) {
        tipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}