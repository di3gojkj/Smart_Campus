package com.smartcampus.msAsignatura.controller;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.service.AsignaturaService;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

    private static final Logger logger = LoggerFactory.getLogger(AsignaturaController.class);
    private final AsignaturaService asignaturaService;

    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    /* GET: para listar todas las Asignaturas */
    @GetMapping
    public ResponseEntity<List<AsignaturaResponseDTO>> ListarTodas(){
        logger.debug("GET /api/asignaturas - Listando todo el catalogo");
        return ResponseEntity.ok(asignaturaService.listarTodas());
    }

    /* GET: Buscar Asignatura por su ID unico */
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponseDTO> buscarPorId(@RequestParam Long id) {
        logger.debug("GET /api/asignaturas/{} - Buscando ramo", id);
        return ResponseEntity.ok(asignaturaService.buscarPorId(id));
    }
    
    /* GET: Buscar ramos filtrando por nombre (Ej: /api/asignaturas/buscar?nombre=Calculo) */
    @GetMapping("/buscar")
    public ResponseEntity<List<AsignaturaResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        logger.debug("GET /api/asignaturas/buscar?nombre={} - Filtrando", nombre);
        return ResponseEntity.ok(asignaturaService.buscarPorNombre(nombre));
    }
    
    /* POST: Crea una nueva Asignatura, devuelve 201*/
    @PostMapping
    public ResponseEntity<AsignaturaResponseDTO> crear(@Valid @RequestBody
        AsignaturaRequestDTO dto) {
        logger.info("POST /api/asignaturas - Registrando ramo");
        AsignaturaResponseDTO creado = asignaturaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /* PUT: Modifica una asignatura Asignatura existente */
    @PutMapping("path/{id}")
    public ResponseEntity<AsignaturaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AsignaturaRequestDTO dto) {
        logger.info("PUT /api/asignaturas/{} - Actualizando ramo", id);
        return ResponseEntity.ok(asignaturaService.actualizar(id, dto));
    }

    /* DELETE: Eliminar una asignatura (204 No Content) */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.warn("DELETE /api/asignaturas/{} - Borrando ramo", id);
        asignaturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
