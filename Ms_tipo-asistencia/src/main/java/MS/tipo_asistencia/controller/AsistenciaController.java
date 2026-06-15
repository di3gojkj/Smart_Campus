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

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.service.AsistenciaService;

// ANOTACIONES DE SWAGGER
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
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
@Tag(name = "Control de Asistencias", description = "Endpoints para el registro, actualización y consulta de la asistencia diaria de los alumnos")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las asistencias", 
        description = "Retorna el listado histórico completo de registros de asistencia almacenados en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de asistencias recuperada con éxito")
    })
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(asistenciaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener asistencia por ID", 
        description = "Busca y devuelve los detalles de un registro de asistencia específico mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro de asistencia localizado exitosamente"),
        @ApiResponse(
            responseCode = "404", 
            description = "No se encontró ningún registro de asistencia con el ID provisto",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(
        @Parameter(description = "ID único del registro de asistencia a consultar", example = "15", required = true)
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(
        summary = "Registrar una nueva asistencia", 
        description = "Da de alta un registro de asistencia para un estudiante, validando los campos obligatorios del cuerpo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Asistencia registrada correctamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Fallo de validación. Los datos enviados contienen errores o formatos inválidos",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar una asistencia existente", 
        description = "Modifica los datos de un registro de asistencia específico (como el estado o fecha) utilizando su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asistencia modificada exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "La estructura del Request contiene errores de validación",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No se pudo actualizar. El ID de asistencia provisto no existe",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<AsistenciaResponseDTO> actualizar(
        @Parameter(description = "ID único del registro de asistencia a modificar", example = "15", required = true)
        @PathVariable Long id, 
        @Valid @RequestBody AsistenciaRequestDTO dto
    ) {
        return ResponseEntity.ok(asistenciaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remover un registro de asistencia", 
        description = "Elimina permanentemente de la base de datos el registro de asistencia asociado al ID enviado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "24", description = "Registro de asistencia eliminado de forma exitosa"),
        @ApiResponse(
            responseCode = "404", 
            description = "El ID del registro no fue localizado en el sistema",
            content = @Content(schema = @Schema(name = "ErrorResponse"))
        )
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID único del registro de asistencia a eliminar", example = "15", required = true)
        @PathVariable Long id
    ) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
