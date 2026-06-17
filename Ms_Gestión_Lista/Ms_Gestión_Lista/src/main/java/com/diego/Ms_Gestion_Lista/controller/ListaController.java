package com.diego.Ms_Gestion_Lista.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.Ms_Gestion_Lista.dto.ListaRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.ListaResponseDTO;
import com.diego.Ms_Gestion_Lista.service.AcademicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@RequiredArgsConstructor
@Tag(name = "Inscripciones (Listas)", description = "Operaciones para matricular alumnos en cursos")
public class ListaController {

    private static final Logger logger = LoggerFactory.getLogger(ListaController.class);
    private final AcademicoService academicoService;

    @Operation(summary = "Listar todas las inscripciones", description = "Obtiene un listado completo de todos los alumnos inscritos en cursos.")
    @GetMapping
    public ResponseEntity<List<ListaResponseDTO>> listar() {
        logger.info("Recibida petición HTTP GET para listar todas las listas académicas");
        return ResponseEntity.ok(academicoService.obtenerTodasLasListas());
    }

    @Operation(summary = "Buscar inscripción por ID", description = "Busca el registro de una lista específica.")
    @GetMapping("/{id}")
    public ResponseEntity<ListaResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        logger.info("Recibida petición HTTP GET para buscar Lista ID: {}", id);
        return ResponseEntity.ok(academicoService.obtenerListaPorId(id));
    }

    @Operation(summary = "Matricular alumno", description = "Crea una nueva inscripción vinculando un usuario y un curso.")
    @PostMapping
    public ResponseEntity<ListaResponseDTO> insertar(@Valid @RequestBody ListaRequestDTO dto) {
        logger.info("Recibida petición HTTP POST para crear registro de Lista");
        return ResponseEntity.status(HttpStatus.CREATED).body(academicoService.crearLista(dto));
    }
}