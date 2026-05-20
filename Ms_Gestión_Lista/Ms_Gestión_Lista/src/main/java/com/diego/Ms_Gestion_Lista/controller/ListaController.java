package com.diego.Ms_Gestion_Lista.controller;

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
    private final AcademicoService academicoService;

    @GetMapping
    public ResponseEntity<List<ListaResponseDTO>> listar() {
        return ResponseEntity.ok(academicoService.obtenerTodasLasListas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(academicoService.obtenerListaPorId(id));
    }

    @PostMapping
    public ResponseEntity<ListaResponseDTO> insertar(@Valid @RequestBody ListaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(academicoService.crearLista(dto));
    }

}
