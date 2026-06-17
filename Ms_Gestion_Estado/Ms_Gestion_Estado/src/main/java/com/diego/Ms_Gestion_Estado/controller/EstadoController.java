package com.diego.Ms_Gestion_Estado.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estados")
@Tag(name = "Estados", description = "Operaciones CRUD para la administración de los estados del sistema")
public class EstadoController {

    private static final Logger logger = LoggerFactory.getLogger(EstadoController.class);
    private final EstadoService estadoService;

    @Operation(summary = "Listar todos los estados", description = "Retorna una lista completa de los estados registrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EstadoResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> obtenerTodos(){
        logger.info("Recibida petición HTTP GET para listar todos los estados");
        return ResponseEntity.ok(estadoService.obtenerTodos());
    }

    @Operation(summary = "Buscar estado por ID", description = "Obtiene los datos de un estado específico mediante su identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El ID del estado no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(
            @Parameter(description = "ID del estado", example = "1") @PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar el estado ID: {}", id);
        return ResponseEntity.ok(estadoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear nuevo estado", description = "Registra un nuevo estado en la base de datos.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estado creado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición")
    })
    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crear(@Valid @RequestBody EstadoRequestDTO dto){
        logger.info("Recibida petición HTTP POST para crear estado: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.guardar(dto));
    }
}
