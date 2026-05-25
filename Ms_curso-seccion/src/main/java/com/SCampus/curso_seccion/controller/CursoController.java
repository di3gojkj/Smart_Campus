package com.SCampus.curso_seccion.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> obtenerTodos() {
        logger.info("Petición HTTP GET recibida en /api/cursos");
        return ResponseEntity.ok(cursoService.obtenerTodos());
    }

    @PostMapping("/guardar")
    public ResponseEntity<CursoResponseDTO> guardar(@Valid @RequestBody CursoResponseDTO curs) {
        logger.info("Petición HTTP POST recibida en /api/cursos/guardar");
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardarCurso(curs));
    }
}