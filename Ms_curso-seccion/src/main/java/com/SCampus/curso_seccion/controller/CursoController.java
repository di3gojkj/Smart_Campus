package com.SCampus.curso_seccion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.service.CursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService cursoService;
    //creo los endpoints necesarios para el microservicio
    //get, put,post,patch,delete

    //GET --> todos los libros
    @GetMapping()
    public ResponseEntity<List<CursoResponseDTO>>obtenerTodos(){
        return ResponseEntity.ok(cursoService.obtenerTodos());
    }
    //POST --> agregar un nuevo libro
    @PostMapping("/guardar")
    public ResponseEntity<CursoResponseDTO> guardar(@Valid @RequestBody CursoResponseDTO curs){
        return ResponseEntity.status(201).body(cursoService.guardarCurso(curs));
    }
}
