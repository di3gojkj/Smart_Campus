package com.smartCampus.Ms_Evaluacion.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.EvaluacionService;

@RestController
@RequestMapping("/api/evaluacion")
public class EvaluacionController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionController.class);

    private final EvaluacionService service;

    // Inyección por constructor (Consistente con los otros servicios)
    public EvaluacionController(EvaluacionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> listarTodas() {
        logger.info("Recibida petición: Listar todas las evaluaciones");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("Recibida petición: Buscar evaluación ID: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Endpoint para buscar por tipo
    @GetMapping("/tipo/{idTipo}")
    public ResponseEntity<List<EvaluacionResponseDTO>> buscarPorTipo(@PathVariable Long idTipo) {
        logger.info("Recibida petición: Buscar evaluaciones por tipo ID: {}", idTipo);
        return ResponseEntity.ok(service.buscarPorTipo(idTipo));
    }

    // Endpoint para filtros (nombre y porcentaje mínimo)
    @GetMapping("/buscar")
    public ResponseEntity<List<EvaluacionResponseDTO>> buscarFiltrado(
            @RequestParam String nombre, 
            @RequestParam Double minPorcentaje) {
        logger.info("Recibida petición: Buscar filtrado - Nombre: {}, MinPorcentaje: {}", nombre, minPorcentaje);
        return ResponseEntity.ok(service.buscarFiltrado(nombre, minPorcentaje));
    }

    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crear(@RequestBody EvaluacionRequestDTO dto) {
        logger.info("Recibida petición: Crear evaluación: {}", dto.getNombre());
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> actualizar(@PathVariable Long id, @RequestBody EvaluacionRequestDTO dto) {
        logger.info("Recibida petición: Actualizar evaluación ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibida petición: Eliminar evaluación ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
