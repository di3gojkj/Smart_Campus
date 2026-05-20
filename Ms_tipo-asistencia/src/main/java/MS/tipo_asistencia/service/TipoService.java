package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoService {

    private TipoResponseDTO mapToDTO(Tipo t) {
        return new TipoResponseDTO(t.getId(), t.getNombre());
    }

    public List<TipoResponseDTO> obtenerTodas() {
        return tipoRepository.findAll().stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<TipoResponseDTO> obtenerPorId(Long id) {
        return tipoRepository.findById(id).map(this::mapToDTO);
    }

    // Expone la entidad para uso interno del ProductoService
    public Tipo buscarEntidadPorId(Long id) {
        return tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Tipo no encontrado con id: " + id));
    }

    public TipoResponseDTO guardar(TipoRequestDTO dto) {
        Tipo t = new Tipo(null, dto.getNombre());
        return mapToDTO(tipoRepository.save(t));
    }

    public Optional<TipoResponseDTO> actualizar(Long id, TipoRequestDTO dto) {
        return tipoRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            return mapToDTO(tipoRepository.save(existente));
        });
    }

    public void eliminar(Long id) {
        tipoRepository.deleteById(id);
    }

}
