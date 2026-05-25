package com.diego.Ms_Gestion_Estado.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.service.EstadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estados")
public class EstadoController {

    private static final Logger logger = LoggerFactory.getLogger(EstadoController.class);
    private final EstadoService estadoService;

    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> obtenerTodos(){
        logger.info("Recibida petición HTTP GET para listar todos los estados");
        return ResponseEntity.ok(estadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar el estado ID: {}", id);
        return ResponseEntity.ok(estadoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crear(@Valid @RequestBody EstadoRequestDTO dto){
        logger.info("Recibida petición HTTP POST para crear estado: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.guardar(dto));
    }
}
