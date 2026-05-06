package com.smartcampus.msAsignatura.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.service.AsignaturaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @GetMapping("")
    public ResponseEntity<List<AsignaturaResponseDTO>> Listar(){
        return ResponseEntity.ok(asignaturaService.obtenerTodos());
    }

    @PostMapping("")
    public ResponseEntity<AsignaturaResponseDTO> crear(@Valid @RequestBody
        AsignaturaRequestDTO dto) {
        return ResponseEntity.ok(asignaturaService.guardar(dto));
    }
}
