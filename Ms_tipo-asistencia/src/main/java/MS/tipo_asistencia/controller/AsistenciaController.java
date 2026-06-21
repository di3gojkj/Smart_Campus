package MS.tipo_asistencia.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.service.AsistenciaService;

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
@RequestMapping("/api/asistencias")
@RequiredArgsConstructor
@Tag(name = "Asistencias", description = "Endpoints para la gestión, registro y enriquecimiento distribuido de asistencia diaria")
public class AsistenciaController {

    private static final Logger logger = LoggerFactory.getLogger(AsistenciaController.class);
    private final AsistenciaService asistenciaService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las asistencias enriquecidas", 
        description = "Retorna una lista completa con todas las asistencias e inyecta dinámicamente los metadatos de inscripción desde el Ms de Gestión Lista."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de asistencias recuperada y enriquecida de forma exitosa",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AsistenciaResponseDTO.class))
        )
    })
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas() {
        logger.info("Petición HTTP GET recibida en /api/asistencias para recuperar el listado consolidado");
        return ResponseEntity.ok(asistenciaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener una asistencia enriquecida por su ID", 
        description = "Busca y devuelve los datos consolidados de un registro de asistencia específico mediante su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Registro de asistencia localizado y enriquecido de manera exitosa",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AsistenciaResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No se encontró ningún registro de asistencia con el ID proporcionado"
        )
    })
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(
        @Parameter(description = "ID único de la asistencia a consultar", example = "101", required = true)
        @PathVariable Long id
    ) {
        logger.info("Petición HTTP GET recibida para recuperar Asistencia ID consolidada: {}", id);
        return asistenciaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("El registro de Asistencia ID {} no fue localizado en la BD", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @PostMapping
    @Operation(
        summary = "Registrar una nueva asistencia con validación distribuida", 
        description = "Crea un registro de asistencia en la base de datos local tras validar sincrónicamente vía Feign que la inscripción sea íntegra en el Ms Gestión Lista."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Asistencia validada perimetralmente y registrada con éxito",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AsistenciaResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o error de consistencia referencial distribuida"
        )
    })
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO asistenciaRequestDTO) {
        logger.info("Petición HTTP POST recibida para dar de alta la asistencia para la fecha: {}", asistenciaRequestDTO.getFecha());
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.guardar(asistenciaRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar un registro de asistencia", 
        description = "Remueve de forma permanente una asistencia física del sistema a partir de su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Registro de asistencia eliminado con éxito (Sin contenido de retorno)"),
        @ApiResponse(responseCode = "404", description = "Operación cancelada. El ID de la asistencia no existe en el sistema")
    })
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID único del registro de asistencia a remover", example = "101", required = true)
        @PathVariable Long id
    ) {
        logger.info("Petición HTTP DELETE recibida para remover la asistencia ID: {}", id);
        if (asistenciaService.obtenerPorId(id).isEmpty()) {
            logger.warn("No se pudo ejecutar el borrado de asistencia. El ID {} no existe", id);
            return ResponseEntity.notFound().build();
        }
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
