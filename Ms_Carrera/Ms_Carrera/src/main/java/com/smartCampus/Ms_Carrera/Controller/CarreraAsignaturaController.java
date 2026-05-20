package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraAsignaturaService;

@RestController
@RequestMapping("/api/carrera-asignatura")

public class CarreraAsignaturaController {
    private final CarreraAsignaturaService service;

    public CarreraAsignaturaController(CarreraAsignaturaService service) {
        this.service = service;
    }

    // 1. Listar asignaturas de una carrera específica
    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<CarreraAsignaturaResponseDTO>> listarPorCarrera(@PathVariable Long idCarrera) {
        return ResponseEntity.ok(service.listarPorCarrera(idCarrera));
    }

    // 2. Crear una nueva relación
    @PostMapping
    public ResponseEntity<CarreraAsignaturaResponseDTO> crear(@RequestBody CarreraAsignaturaRequestDTO dto) {
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    // 3. Actualizar una relación existente
    @PutMapping("/{id}")
    public ResponseEntity<CarreraAsignaturaResponseDTO> actualizar(@PathVariable Long id, @RequestBody CarreraAsignaturaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // 4. Eliminar una relación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
