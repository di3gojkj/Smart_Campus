package com.smartcampus.msAsignatura.controller;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.service.SemestreService;


import java.util.List;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/semestres") 
public class SemestreController {

    private static final Logger logger = LoggerFactory.getLogger(SemestreController.class);
    private final SemestreService semestreService;

    public SemestreController(SemestreService semestreService) {
        this.semestreService = semestreService;
    }


    /* GET: para listar todos los semestres */
    @GetMapping
    public ResponseEntity<List<SemestreResponseDTO>> listarTodos() {
        logger.debug("GET /api/semestres - Listando todo");
        return ResponseEntity.ok(semestreService.listarTodosCronologicos());   
    }

    /* GET: para Buscar por Id */
    @GetMapping("/{id}")
    public ResponseEntity<SemestreResponseDTO> buscarPorId(@PathVariable Long id){
        logger.debug("GET /api/semestres/{} - Buscando", id);
        return ResponseEntity.ok(semestreService.buscarPorId(id));
    }

    /* POST: para crear nuevo semestre, devuelve 201 Created */
    @PostMapping
    public ResponseEntity<SemestreResponseDTO> crear(@Valid @RequestBody 
        SemestreRequestDTO dto){
        logger.debug("POST /api/semestres - Creando nuevo semestre");
        SemestreResponseDTO creado = semestreService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /* PUT: Actualiza un semestre completo por ID */
    @PutMapping("/{id}")
    public ResponseEntity<SemestreResponseDTO> actualizar(@PathVariable Long id,
        @Valid @RequestBody SemestreRequestDTO dto){
        logger.info("PUT /api/semestres/{} - Actualizando datos", id);
        return ResponseEntity.ok(semestreService.actualizar(id, dto));
    }

    /* DELETE: Borra un semestre, devuelve 204 No content si todo sale bien*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.warn("DELETE /api/semestres/{} - Eliminando registro", id);
        semestreService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
