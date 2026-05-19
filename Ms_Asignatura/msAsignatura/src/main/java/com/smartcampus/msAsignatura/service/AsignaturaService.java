package com.smartcampus.msAsignatura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.DTO.EstadoResponseDTO;
import com.smartcampus.msAsignatura.client.EstadoClient;
import com.smartcampus.msAsignatura.model.Asignatura;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;



@Service
public class AsignaturaService {
    private static final Logger logger = LoggerFactory.getLogger(AsignaturaService.class);

    private final AsignaturaRepository asignaturaRepository;
    private final EstadoClient estadoClient;

    // Inyección por constructor (Best Practice)
    public AsignaturaService(AsignaturaRepository asignaturaRepository, EstadoClient estadoClient) {
        this.asignaturaRepository = asignaturaRepository;
        this.estadoClient = estadoClient;
    }

    @Transactional(readOnly = true)
    public List<AsignaturaResponseDTO> listarTodas() {
        logger.debug("Buscando el listado completo de asignaturas");
        return asignaturaRepository.findAll().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsignaturaResponseDTO buscarPorId(Long id) {
        logger.debug("Buscando Asignatura por ID: {}", id);
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Asignatura no encontrada con ID: {}", id);
                    return new RuntimeException("Asignatura no encontrada con ID: " + id);
                });
        return mapearAResponseDTO(asignatura);
    }

    @Transactional(readOnly = true)
    public List<AsignaturaResponseDTO> buscarPorNombre(String nombre) {
        logger.debug("Filtrando asignaturas que contengan el nombre: {}", nombre);
        return asignaturaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AsignaturaResponseDTO crear(AsignaturaRequestDTO dto) {
        logger.info("Creando nueva asignatura: {} [{}]", dto.getNombre(), dto.getSigla());

        // Guardia de unicidad para la Sigla
        if (asignaturaRepository.findBySiglaIgnoreCase(dto.getSigla()).isPresent()) {
            logger.warn("Intento de duplicación de sigla académica: {}", dto.getSigla());
            throw new IllegalArgumentException("La sigla '" + dto.getSigla() +
             "' ya está asignada a otra asignatura.");
        }

        Asignatura asignatura = mapearAEntidad(dto);
        Asignatura guardada = asignaturaRepository.save(asignatura);
        logger.info("Asignatura creada exitosamente con ID: {}", guardada.getId_Asignatura());
        
        return mapearAResponseDTO(guardada);
    }

    @Transactional
    public AsignaturaResponseDTO actualizar(Long id, AsignaturaRequestDTO dto) {
        logger.info("Actualizando Asignatura ID: {}", id);

        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo actualizar. Asignatura ID: {} no existe", id);
                    return new RuntimeException("Asignatura no encontrada con ID: " + id);
                });

        // Validar que la nueva sigla no pertenezca a otra asignatura distinta
        asignaturaRepository.findBySiglaIgnoreCase(dto.getSigla())
                .ifPresent(existente -> {
                    if (!existente.getId_Asignatura().equals(id)) {
                        logger.warn("Colisión de sigla académica: '{}' pertenece a ID {}", dto.getSigla(), existente.getId_Asignatura());
                        throw new IllegalArgumentException("La sigla '" + dto.getSigla() + "' ya pertenece a otra asignatura.");
                    }
                });

        asignatura.setNombre(dto.getNombre());
        asignatura.setSigla(dto.getSigla());
        asignatura.setIdEstado(dto.getIdEstado()); // Guardamos la referencia numérica al otro MS

        Asignatura actualizada = asignaturaRepository.save(asignatura);
        logger.info("Asignatura ID: {} actualizada de forma exitosa", id);
        
        return mapearAResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando Asignatura ID: {}", id);
        if (!asignaturaRepository.existsById(id)) {
            logger.warn("No se pudo eliminar. Asignatura ID: {} no existe en la BD", id);
            throw new RuntimeException("No se puede eliminar. Asignatura no encontrada con ID: " + id);
        }
        asignaturaRepository.deleteById(id);
        logger.info("Asignatura ID: {} eliminada correctamente", id);
    }

    private AsignaturaResponseDTO mapearAResponseDTO(Asignatura a) {
        AsignaturaResponseDTO dto = new AsignaturaResponseDTO();
        dto.setId_Asignatura(a.getId_Asignatura());
        dto.setNombre(a.getNombre());
        dto.setSigla(a.getSigla());
        dto.setIdEstado(a.getIdEstado());

        /* MAPEO INTELIGENTE: Basado en el ID local */
        dto.setActivo(a.getIdEstado() != null && a.getIdEstado() == 1L);

        try {
            if (a.getIdEstado() != null) {
                // Enriquecemos con el nombre desde el otro MS
                EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(a.getIdEstado());
                dto.setNombreEstado(estado.getNombre());
            }
        } catch (Exception e) {
            // Si el otro MS está abajo, no rompemos nuestro sistema. 
            // Logeamos el error para saber qué pasó y tiramos un valor seguro.
            logger.error("Se nos cayó la conexión con MS Gestión Estado para el ID: {}. Error: {}", 
            a.getIdEstado(), e.getMessage());
            dto.setNombreEstado("Estado no disponible");
        }
        return dto;
    }

    private Asignatura mapearAEntidad(AsignaturaRequestDTO dto) {
        Asignatura a = new Asignatura();
        a.setNombre(dto.getNombre());
        a.setSigla(dto.getSigla());
        a.setIdEstado(dto.getIdEstado());
        return a;
    }
}
