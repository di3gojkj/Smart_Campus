package com.cur_eva.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.service.CursoEvaluacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estados")
public class CursoEvaluacionController {
    private final CursoEvaluacionService cursoEvaluacionService;
    //creo los endpoints necesarios para el microservicio
    //get, put,post,patch,delete

    

    @GetMapping
    public ResponseEntity<List<CursoEvaluacionResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(cursoEvaluacionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoEvaluacionResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        // Si no se encuentra, la excepción gatilla el flujo automático del GlobalExceptionHandler
        return ResponseEntity.ok(cursoEvaluacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoEvaluacionResponseDTO> crear(@Valid @RequestBody CursoEvaluacionRequestDTO dto){//@valid es validacdion de errores como el notblank
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoEvaluacionService.guardar(dto));
    }
}
