package com.smartCampus.Ms_Evaluacion.controller;

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

import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.TipoEvaluacionService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tipo-evaluacion")
public class TipoEvaluacionController {

    private static final Logger logger = LoggerFactory.getLogger(TipoEvaluacionController.class);

    private final TipoEvaluacionService service;

    // Inyección por constructor (Siguiendo tu patrón)
    public TipoEvaluacionController(TipoEvaluacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoEvaluacionResponseDTO>> listarTodos() {
        logger.info("Recibida petición para listar todos los tipos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEvaluacionResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("Recibida petición para buscar tipo con ID: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoEvaluacionResponseDTO> crear(@RequestBody TipoEvaluacionRequestDTO dto) {
        logger.info("Recibida petición para crear tipo: {}", dto.getNombreTipo());
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEvaluacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody TipoEvaluacionRequestDTO dto) {
        logger.info("Recibida petición para actualizar tipo ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibida petición para eliminar tipo ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
