package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraService;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private static final Logger logger = LoggerFactory.getLogger(CarreraController.class);
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<List<CarreraResponseDTO>> listarTodas() {
        logger.info("Recibida petición para listar todas las carreras");
        return ResponseEntity.ok(carreraService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("Recibida petición para buscar carrera con ID: {}", id);
        return ResponseEntity.ok(carreraService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CarreraResponseDTO>> buscarPorFiltro(@RequestParam String filtro) {
        logger.info("Recibida petición de búsqueda por filtro: {}", filtro);
        return ResponseEntity.ok(carreraService.buscarPorFiltro(filtro));
    }

    @PostMapping
    public ResponseEntity<CarreraResponseDTO> crear(@RequestBody CarreraRequestDTO dto) {
        logger.info("Recibida petición para crear carrera: {}", dto.getNombre());
        CarreraResponseDTO creada = carreraService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> actualizar(@PathVariable Long id, @RequestBody CarreraRequestDTO dto) {
        logger.info("Recibida petición para actualizar carrera ID: {}", id);
        return ResponseEntity.ok(carreraService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibida petición para eliminar carrera ID: {}", id);
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
