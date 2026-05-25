package com.SCampus.curso_seccion.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.service.SeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seccion")
@RequiredArgsConstructor
public class SeccionController {

    private static final Logger logger = LoggerFactory.getLogger(SeccionController.class);
    private final SeccionService seccionService;

    @GetMapping
    public ResponseEntity<List<Seccion>> obtenerSecciones() {
        logger.info("Petición HTTP GET recibida en /api/seccion");
        return ResponseEntity.ok(seccionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seccion> obtenerSeccionPorId(@PathVariable Long id) {
        logger.info("Petición HTTP GET recibida para recuperar Sección ID: {}", id);
        return seccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("La Sección ID {} no fue localizada en la BD", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    @PostMapping
    public ResponseEntity<Seccion> crear(@Valid @RequestBody Seccion seccion) {
        logger.info("Petición HTTP POST recibida para dar de alta la sección: {}", seccion.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.guardar(seccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seccion> actualizar(@PathVariable Long id, @Valid @RequestBody Seccion seccion) {
        logger.info("Petición HTTP PUT recibida para actualizar la sección ID: {}", id);
        return seccionService.obtenerPorId(id)
                .map(existente -> {
                    seccion.setId(id);
                    return ResponseEntity.ok(seccionService.guardar(seccion));
                })
                .orElseGet(() -> {
                    logger.warn("Intento fallido de actualización. Sección ID {} inexistente", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Petición HTTP DELETE recibida para remover la sección ID: {}", id);
        if (seccionService.obtenerPorId(id).isEmpty()) {
            logger.warn("No se pudo ejecutar el borrado. Sección ID {} no existe", id);
            return ResponseEntity.notFound().build();
        }
        seccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}