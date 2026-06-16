package com.diego.MS_Gestion_Usuario.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.service.UsuarioService;

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

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones CRUD para la administración del registro de usuarios del Smart Campus")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista completa de los usuarios registrados en el sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada exitosamente", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UsuarioResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        logger.info("Recibida petición HTTP GET para listar todos los usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Obtiene los datos de un usuario específico mediante su identificador único.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no localizado en la base de datos", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(
            @Parameter(description = "ID numérico del usuario a buscar", example = "1", required = true) 
            @PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar usuario por ID: {}", id);
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @Operation(summary = "Buscar usuario por Correo", description = "Ruta consumida internamente vía Feign Client por el MS_Confi para validar el inicio de sesión.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Correo no registrado")
    })
    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorCorreo(
            @Parameter(description = "Correo exacto del usuario", example = "diego.rivas@duocuc.cl", required = true) 
            @PathVariable("correo") String correo) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(correo));
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario validando existencia de RUT/Correo, verificando estados por Feign y encriptando su clave.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación (Ej: campos faltantes o formato de correo inválido)", 
                     content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @Valid @RequestBody UsuarioRequestDTO dto) {
        logger.info("Recibida petición HTTP POST para registrar un nuevo usuario");
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(dto));
    }
}