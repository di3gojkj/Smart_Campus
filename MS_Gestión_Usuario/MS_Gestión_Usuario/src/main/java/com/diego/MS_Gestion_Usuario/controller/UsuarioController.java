package com.diego.MS_Gestion_Usuario.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.MS_Gestion_Usuario.dto.UsuarioRequestDTO;
import com.diego.MS_Gestion_Usuario.dto.UsuarioResponseDTO;
import com.diego.MS_Gestion_Usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        logger.info("Recibida petición HTTP GET para listar todos los usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar usuario por ID: {}", id);
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        logger.info("Recibida petición HTTP POST para registrar al usuario con RUT: {}", dto.getRut());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(dto));
    }
}
