package com.smartcampus.msAsignatura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.DTO.EstadoResponseDTO;
import com.smartcampus.msAsignatura.client.EstadoClient;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;




@Service

public class SemestreService {

    private static final Logger logger = LoggerFactory.getLogger(SemestreService.class);

    private final SemestreRepository semestreRepository;
    private final EstadoClient estadoClient;


    public SemestreService(SemestreRepository semestreRepository, EstadoClient estadoClient) {
        this.semestreRepository = semestreRepository;
        this.estadoClient = estadoClient;
    }

    @Transactional(readOnly = true)
    public List<SemestreResponseDTO> listarTodosCronologicos(){
        logger.debug("Buscando todos los semestres ordenados cronologicamente");
        return semestreRepository.listarSemestreCronologicos().stream()
        .map(this::mapearAResponseDTO)
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SemestreResponseDTO buscarPorId(Long id) {
        logger.debug("Buscando Por Id: {}", id);
        Semestre semestre = semestreRepository.findById(id)
        .orElseThrow(() -> {
            logger.warn("Semestre no encontrado con ID: {}", id);
            return new RuntimeException("Semestre no encontrado con ID: "+ id);
        });
        return mapearAResponseDTO(semestre);
    }

    @Transactional
    public SemestreResponseDTO crear(SemestreRequestDTO dto){
        logger.info("Creando nuevo semestre: {}", dto.getNombre());

        // Proteccion contra semestres duplicados

        if(semestreRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()) {
            logger.warn("Intento de duplicacion: El semestre '{}' ya existe", dto.getNombre());
            throw new IllegalArgumentException("El semestre '" + dto.getNombre() + "' ya existe.");
        }

        Semestre semestre = mapearAEntidad(dto);
        Semestre guardado = semestreRepository.save(semestre);
        logger.info("Semestre creado exitosamente con ID: {}", guardado.getIdSemestre());
        
        return mapearAResponseDTO(guardado);
    }

    @Transactional
    public SemestreResponseDTO actualizar(Long id, SemestreRequestDTO dto) {
        logger.info("Actualizando Semestre ID: {}", id);
        
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se puede actualizar. Semestre no encontrado con ID: {}", id);
                    return new RuntimeException("Semestre no encontrado con ID: " + id);
                });

        // Validar que el nuevo nombre no colisione con otro ID existente
        semestreRepository.findByNombreIgnoreCase(dto.getNombre())
                .ifPresent(existente -> {
                    if (!existente.getIdSemestre().equals(id)) {
                        logger.warn("Colisión de nombres al actualizar Semestre ID {}: '{}' ya pertenece a ID {}", 
                                id, dto.getNombre(), existente.getIdSemestre());
                        throw new IllegalArgumentException("Ya existe otro semestre con el nombre: " + dto.getNombre());
                    }
                });

        semestre.setNombre(dto.getNombre());
        semestre.setIdEstado(dto.getIdEstado());
        Semestre actualizado = semestreRepository.save(semestre);
        logger.info("Semestre ID: {} actualizado exitosamente", id);
        
        return mapearAResponseDTO(actualizado);
    }         

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando Semestre ID: {}", id);
        if (!semestreRepository.existsById(id)) {
            logger.warn("No se pudo eliminar. Semestre ID: {} no existe en la BD", id);
            throw new RuntimeException("No se puede eliminar. Semestre no encontrado con ID: " + id);
        }
        semestreRepository.deleteById(id);
        logger.info("Semestre ID: {} eliminado correctamente", id);
    }


    private SemestreResponseDTO mapearAResponseDTO(Semestre s) {
        SemestreResponseDTO dto = new SemestreResponseDTO();
        dto.setIdSemestre(s.getIdSemestre());
        dto.setNombre(s.getNombre());
        dto.setIdEstado(s.getIdEstado());

        /* MAPEO INTELIGENTE: Si idEstado es 1L, el semestre esta ACTIVO. */ 
        /* Funciona localmente sin depender de otros microservicios */
        dto.setActivo(s.getIdEstado() != null && s.getIdEstado() == 1L);

        // Intentamos obtener el nombre descriptivo del estado via Feign
        try {
            if (s.getIdEstado() != null) {
                EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(s.getIdEstado());
                dto.setNombreEstado(estado.getNombre());
            }
        } catch (Exception e) {
            logger.error("Error al conectar con MS Gestión Estado para Semestre ID: {}. Error: {}", 
            s.getIdSemestre(), e.getMessage());
            dto.setNombreEstado("Estado no disponible");
        }

        return dto;
    }

    private Semestre mapearAEntidad(SemestreRequestDTO dto) {
        Semestre s = new Semestre();
        s.setNombre(dto.getNombre());
        s.setIdEstado(dto.getIdEstado());
        return s;
    }
}
