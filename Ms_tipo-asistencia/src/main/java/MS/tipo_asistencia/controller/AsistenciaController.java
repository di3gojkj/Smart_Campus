package MS.tipo_asistencia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {
    
    private final AsistenciaService asistenciaService;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(asistenciaService.obtenerTodos());
    }

     // GET /api/asistencia/{id} → 200 OK o 404
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return asistenciaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/asistencia/tipo/1
    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerPorTipo(
            @PathVariable Long tipoId) {
        return ResponseEntity.ok(asistenciaService.obtenerPorTipo(tipoId));
    }

    // POST /api/asistencia → 201 Created
    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(
            @Valid @RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.status(201).body(asistenciaService.guardar(dto));
    }

    // PUT /api/asistencia/{id} → 200 OK o 404
    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody AsistenciaRequestDTO dto) {
        return asistenciaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/asistencia/{id} → 204 No Content o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (asistenciaService.obtenerPorId(id).isEmpty())
            return ResponseEntity.notFound().build();
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
}
