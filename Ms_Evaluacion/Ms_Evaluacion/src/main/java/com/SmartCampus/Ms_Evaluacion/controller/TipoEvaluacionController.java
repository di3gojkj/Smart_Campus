package com.SmartCampus.Ms_Evaluacion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SmartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.SmartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.SmartCampus.Ms_Evaluacion.service.TipoEvaluacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/Tipo-evaluaciones")
@RequiredArgsConstructor

public class TipoEvaluacionController {

    private final TipoEvaluacionService tipoEvaluacionService;

    @PostMapping("")
    public ResponseEntity<TipoEvaluacionResponseDTO> crear(@Valid @RequestBody 
        TipoEvaluacionRequestDTO dto) {

        return ResponseEntity.ok(tipoEvaluacionService.guardar(dto));

    }

    @GetMapping("")
    public ResponseEntity<List<TipoEvaluacionResponseDTO>> Listar() {
        return ResponseEntity.ok(tipoEvaluacionService.listarTodos());
    }
    
    

}
