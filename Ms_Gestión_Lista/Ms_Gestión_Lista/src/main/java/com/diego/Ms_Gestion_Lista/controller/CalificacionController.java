package com.diego.Ms_Gestion_Lista.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.diego.Ms_Gestion_Lista.dto.CalificacionRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.CalificacionResponseDTO;
import com.diego.Ms_Gestion_Lista.service.AcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private static final Logger logger = LoggerFactory.getLogger(CalificacionController.class);
    private final AcademicoService academicoService;

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> evaluar(@Valid @RequestBody CalificacionRequestDTO dto) {
        logger.info("Recibida petición HTTP POST para evaluar a Lista ID: {}", dto.getIdLista());
        return ResponseEntity.status(HttpStatus.CREATED).body(academicoService.registrarCalificacion(dto));
    }

    @GetMapping("/lista/{idLista}")
    public ResponseEntity<List<CalificacionResponseDTO>> listarPorLista(@PathVariable("idLista") Long idLista) {
        logger.info("Recibida petición HTTP GET de calificaciones para la Lista ID: {}", idLista);
        return ResponseEntity.ok(academicoService.obtenerCalificacionesPorLista(idLista));
    }
}
