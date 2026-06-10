package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraService;

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
@RequestMapping("/api/carreras")

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Carreras",
    description = "Gestion academica de carrera"
)
public class CarreraController {

    private static final Logger logger = LoggerFactory.getLogger(CarreraController.class);
    private final CarreraService carreraService;


    @Operation(
        summary = "Listar carreras",
        description = "Retorna todas las carreras"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de carreras retornada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CarreraResponseDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<CarreraResponseDTO>> listarTodas() {
        log.info("[CarreraController] Listando todas las carreras");
        return ResponseEntity.ok(carreraService.listarTodas());
    }

    @Operation(
        summary = "Buscar carrera por ID",
        description = "Busca y retorna la carrera con el ID indicado."+
        "Puede retornar 404 en caso que no la encuentre en la tabla"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Carrera encontrada mediante su ID",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CarreraResponseDTO.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Carrera no encontrada en la BD",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> buscarPorId(
        @Parameter(
            description = "ID de la carrera a buscar",
            example = "1",
            required = true
        )
        @PathVariable Long id
    ){
        log.info("[CarreraController] GET (api/carreras/{}", id);
        return ResponseEntity.ok(carreraService.buscarPorId(id));
    }

    
    @Operation(
        summary = "Crear nueva Carrera",
        description = "Registra una nueva Carrera en la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Carrera creada correctamente"
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
    public ResponseEntity<CarreraResponseDTO> crear(
        @Valid @RequestBody CarreraRequestDTO dto
    ){
        CarreraResponseDTO creado = carreraService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(
        summary = "actualiza una Carrera existente",
        description = "actualiza una Carrera de la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Carrera actualizada correctamente",
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
    public ResponseEntity<CarreraResponseDTO> actualizar(@PathVariable Long id, 
        @Valid @RequestBody CarreraRequestDTO dto) {
        log.info("[CarreraController] PUT Actualizando carrera ID: {}", id);
        return ResponseEntity.ok(carreraService.actualizar(id, dto));
    }

    @Operation(
        summary = "elimina una Carrera existente",
        description = "elimina una Carrera existente en la BD"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Carrera eliminada correctamente"
        ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada invalidos"
                )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[CarreraController] DELETE Eliminando carrera ID: {}", id);
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
