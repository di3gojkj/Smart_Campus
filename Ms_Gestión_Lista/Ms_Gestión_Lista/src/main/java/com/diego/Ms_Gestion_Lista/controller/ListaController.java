package com.diego.Ms_Gestion_Lista.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.Ms_Gestion_Lista.dto.ListaRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.ListaResponseDTO;
import com.diego.Ms_Gestion_Lista.service.AcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@RequiredArgsConstructor
public class ListaController {

    private static final Logger logger = LoggerFactory.getLogger(ListaController.class);
    private final AcademicoService academicoService;

    @GetMapping
    public ResponseEntity<List<ListaResponseDTO>> listar() {
        logger.info("Recibida petición HTTP GET para listar todas las listas académicas");
        return ResponseEntity.ok(academicoService.obtenerTodasLasListas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar Lista ID: {}", id);
        return ResponseEntity.ok(academicoService.obtenerListaPorId(id));
    }

    @PostMapping
    public ResponseEntity<ListaResponseDTO> insertar(@Valid @RequestBody ListaRequestDTO dto) {
        logger.info("Recibida petición HTTP POST para crear registro de Lista");
        return ResponseEntity.status(HttpStatus.CREATED).body(academicoService.crearLista(dto));
    }
}
