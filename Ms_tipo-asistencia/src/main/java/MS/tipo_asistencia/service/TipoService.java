package MS.tipo_asistencia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.TipoRepository;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoService {

    private static final Logger logger = LoggerFactory.getLogger(TipoService.class);
    
    private final TipoRepository TipoRepository;
    

    @Transactional(readOnly = true)
    public List<TipoResponseDTO> obtenerTodas() {
        logger.info("Listando todos los tipos");
        return TipoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoResponseDTO obtenerPorId(Long id) {
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
    

        return dto;
    }

    private Tipo mapearAEntidad(TipoRequestDTO dto) {
        Tipo t = new Tipo();
        t.setNombre(dto.getNombre());
        t.setIdTipo(dto.getTipoId());
        return t;
    }

}
