package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.TipoRepository;
import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoService {

    private static final Logger logger = LoggerFactory.getLogger(TipoService.class);
    
    private final TipoRepository TipoRepository;
    

    private TipoResponseDTO mapToDTO(Tipo t) {
        return new TipoResponseDTO(t.getIdTipo(), t.getNombre());
    }

    @Transactional(readOnly = true)
    public List<TipoResponseDTO> listarTodas() {
        logger.info("Listando todos los tipos");
        return TipoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoResponseDTO buscarPorId(Long id) {
        logger.info("Buscando tipo con ID: {}", id);
        Tipo tipo = TipoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Carrera no encontrada con ID: {}", id);
                    return new RuntimeException("Carrera no encontrada con ID: " + id);
                });
        return toResponseDTO(tipo);
    }

    @Transactional
    public TipoResponseDTO crear(TipoRequestDTO dto) {
        logger.info("Creando nueva carrera: {} [{}]", dto.getNombre());

        Tipo tipo = mapearAEntidad(dto);
        Tipo guardada = TipoRepository.save(tipo);
        logger.info("Tipo creada exitosamente con ID: {}", guardada.getIdTipo());

        return toResponseDTO(guardada);
    }

    @Transactional
    public TipoResponseDTO actualizar(Long id, TipoRequestDTO dto) {
        logger.info("Actualizando tipo con ID: {}", id);

        Tipo tipo = TipoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo actualizar. Tipo ID: {} no existe", id);
                    return new RuntimeException("tipo no encontrada con ID: " + id);
                });

        tipo.setNombre(dto.getNombre());
        tipo.setIdTipo(dto.getIdTipo());

        Tipo actualizada = TipoRepository.save(tipo);
        logger.info("Carrera ID: {} actualizada exitosamente", id);

        return toResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando tipo con ID: {}", id);
        if (!TipoRepository.existsById(id)) {
            logger.warn("No se pudo eliminar. tipo ID: {} no existe", id);
            throw new RuntimeException("No se puede eliminar. Tipo no encontrada con ID: " + id);
        }
        TipoRepository.deleteById(id);
        logger.info("Tipo ID: {} eliminada correctamente", id);
    }

    private TipoResponseDTO toResponseDTO(Tipo t) {
        TipoResponseDTO dto = new TipoResponseDTO();
        dto.setIdTipo(t.getIdTipo());
        dto.setNombre(t.getNombre());

        return dto;
    }

    private Tipo mapearAEntidad(TipoRequestDTO dto) {
        Tipo t = new Tipo();
        t.setNombre(dto.getNombre());
        t.setIdTipo(dto.getIdTipo());
        return t;
    }

}
