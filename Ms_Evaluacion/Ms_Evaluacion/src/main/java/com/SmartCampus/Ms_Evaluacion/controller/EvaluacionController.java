package com.SmartCampus.Ms_Evaluacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SmartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.SmartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.SmartCampus.Ms_Evaluacion.service.EvaluacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor

public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping("")
    public ResponseEntity<EvaluacionResponseDTO> crear(@Valid @RequestBody
        EvaluacionRequestDTO dto) {
        return ResponseEntity.ok(evaluacionService.guardar(dto));
    }
    

}
