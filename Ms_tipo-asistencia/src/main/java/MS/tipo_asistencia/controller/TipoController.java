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

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.service.TipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tipo")
@RequiredArgsConstructor
public class TipoController {
    private final TipoService tipoService;

    @GetMapping
    public ResponseEntity<List<TipoResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(tipoService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return tipoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoResponseDTO> crear(
            @Valid @RequestBody TipoRequestDTO dto) {
        return ResponseEntity.status(201).body(tipoService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoResponseDTO> actualizar(
            @PathVariable Long id, @Valid @RequestBody TipoRequestDTO dto) {
        return tipoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (tipoService.obtenerPorId(id).isEmpty())
            return ResponseEntity.notFound().build();
        tipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
