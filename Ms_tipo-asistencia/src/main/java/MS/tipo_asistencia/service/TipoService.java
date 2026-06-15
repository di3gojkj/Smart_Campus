package MS.tipo_asistencia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoService {

    private final TipoRepository tipoRepository;

    @Transactional(readOnly = true)
    public List<TipoResponseDTO> obtenerTodas() {
        log.info("Consultando el catálogo completo de tipos de asistencia");
        return tipoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoResponseDTO obtenerPorId(Long id) {
        log.info("Buscando tipo de asistencia con ID: {}", id);
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tipo de asistencia no encontrado con ID: {}", id);
                    return new RuntimeException("Tipo de asistencia no encontrado con ID: " + id);
                });
        return toResponseDTO(tipo);
    }

    @Transactional
    public TipoResponseDTO crear(TipoRequestDTO dto) {
        log.info("Registrando nueva opción en el catálogo: {}", dto.getNombre());

        Tipo tipo = mapearAEntidad(dto);
        Tipo guardada = tipoRepository.save(tipo);
        log.info("Tipo de asistencia creado exitosamente en BD con ID: {}", guardada.getIdTipo());

        return toResponseDTO(guardada);
    }

    @Transactional
    public TipoResponseDTO actualizar(Long id, TipoRequestDTO dto) {
        log.info("Actualizando tipo de asistencia con ID: {}", id);

        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Tipo ID: {} no existe", id);
                    return new RuntimeException("Tipo de asistencia no encontrado con ID: " + id);
                });

        tipo.setNombre(dto.getNombre());

        Tipo actualizada = tipoRepository.save(tipo);
        log.info("Tipo de asistencia ID: {} actualizado exitosamente", id);

        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando clasificación del catálogo con ID: {}", id);
        if (!tipoRepository.existsById(id)) {
            log.warn("No se pudo ejecutar el borrado. Tipo ID: {} no existe", id);
            throw new RuntimeException("No se puede eliminar. Tipo de asistencia no encontrado con ID: " + id);
        }
        tipoRepository.deleteById(id);
        log.info("Tipo de asistencia ID: {} removido correctamente del catálogo", id);
    }

    private TipoResponseDTO toResponseDTO(Tipo t) {
        TipoResponseDTO dto = new TipoResponseDTO();
        dto.setIdTipo(t.getIdTipo());
        dto.setNombre(t.getNombre()); // CORREGIDO: Ahora sí mapea el nombre de salida al DTO
        return dto;
    }

    private Tipo mapearAEntidad(TipoRequestDTO dto) {
        Tipo t = new Tipo();
        t.setNombre(dto.getNombre());
        // CORREGIDO: Se elimina la asignación manual setIdTipo() ya que es autoincrementable (IDENTITY) en la BD
        return t;
    }
}
