package com.smartcampus.msAsignatura.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.service.SemestreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/semestres")
@RequiredArgsConstructor
public class SemestreController {

    private final SemestreService semestreService;

    @GetMapping("")
    public ResponseEntity<List<SemestreResponseDTO>> Listar() {
        return ResponseEntity.ok(semestreService.obtenerTodos());
    }

    public ResponseEntity<SemestreResponseDTO> crear(@Valid @RequestBody 
        SemestreRequestDTO dto){
        return ResponseEntity.ok(semestreService.guardar(dto));
    }
    

}
