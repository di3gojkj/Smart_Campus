package com.diego.Ms_Gestion_Estado.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.service.EstadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estados")
public class EstadoController {
    private final EstadoService estadoService;
    //creo los endpoints necesarios para el microservicio
    //get, put,post,patch,delete

    

    @GetMapping
    public ResponseEntity<List<EstadoResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(estadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        // Si no se encuentra, la excepción gatilla el flujo automático del GlobalExceptionHandler
        return ResponseEntity.ok(estadoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstadoResponseDTO> crear(@Valid @RequestBody EstadoRequestDTO dto){//@valid es validacdion de errores como el notblank
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.guardar(dto));
    }
}
