package MS.tipo_asistencia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.AsistenciaRepository;
import MS.tipo_asistencia.repository.TipoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    // CORREGIDO: El nombre de la variable debe empezar con minúscula 'tipoRepository'
    private final TipoRepository tipoRepository;

    @Transactional(readOnly = true)
    public List<AsistenciaResponseDTO> obtenerTodas() {
        log.info("Listando todas las asistencias académicas");
        return asistenciaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsistenciaResponseDTO obtenerPorId(Long id) {
        log.info("Buscando registro de asistencia con ID: {}", id);
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Asistencia no encontrada con ID: {}", id);
                    return new RuntimeException("Asistencia no encontrada con ID: " + id);
                });
        return toResponseDTO(asistencia);
    }

    @Transactional
    public AsistenciaResponseDTO crear(AsistenciaRequestDTO dto) {
        log.info("Creando nueva asistencia para la fecha: {}", dto.getFecha());

        Tipo tipo = tipoRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new RuntimeException("No se puede crear asistencia. Tipo ID " + dto.getTipoId() + " no existe"));

        Asistencia asistencia = mapearAEntidad(dto, tipo);
        Asistencia guardada = asistenciaRepository.save(asistencia);
        log.info("Asistencia creada exitosamente con ID: {}", guardada.getIdAsistencia());

        return toResponseDTO(guardada);
    }

    @Transactional
    public AsistenciaResponseDTO actualizar(Long id, AsistenciaRequestDTO dto) {
        log.info("Actualizando asistencia con ID: {}", id);

        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se pudo actualizar. Asistencia ID: {} no existe", id);
                    return new RuntimeException("Asistencia no encontrada con ID: " + id);
                });

        Tipo tipo = tipoRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new RuntimeException("Tipo ID " + dto.getTipoId() + " no existe"));

        asistencia.setFecha(dto.getFecha());
        asistencia.setTipo(tipo);

        Asistencia actualizada = asistenciaRepository.save(asistencia);
        log.info("Asistencia ID: {} actualizada exitosamente", id);

        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Intentando eliminar asistencia con ID: {}", id);
        if (!asistenciaRepository.existsById(id)) {
            log.warn("No se pudo eliminar. Asistencia ID: {} no existe", id);
            throw new RuntimeException("No se puede eliminar. Asistencia no encontrada con ID: " + id);
        }
        asistenciaRepository.deleteById(id);
        log.info("Asistencia ID: {} eliminada correctamente", id);
    }

    private AsistenciaResponseDTO toResponseDTO(Asistencia a) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setIdAsistencia(a.getIdAsistencia());
        dto.setFecha(a.getFecha());
        
        if (a.getTipo() != null) {
            TipoResponseDTO tipoDTO = new TipoResponseDTO();
            tipoDTO.setIdTipo(a.getTipo().getIdTipo());
            tipoDTO.setNombre(a.getTipo().getNombre());
            dto.setTipo(tipoDTO);
        }
        return dto;
    }

    private Asistencia mapearAEntidad(AsistenciaRequestDTO dto, Tipo tipo) {
        Asistencia a = new Asistencia();
        a.setFecha(dto.getFecha());
        a.setTipo(tipo);
        return a;
    }
}

