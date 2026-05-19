package com.smartcampus.msAsignatura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;




@Service

public class SemestreService {

    private static final Logger logger = LoggerFactory.getLogger(SemestreService.class);

    private final SemestreRepository semestreRepository;


    public SemestreService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
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
        
        return mapearAResponseDTO(semestre);
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
        Semestre actualizado = semestreRepository.save(semestre);
        logger.info("Semestre ID: {} actualizado exitosamente", id);
        
        return mapearAResponseDTO(actualizado);
    }         

    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando Semestre ID: {}", id);
        if (!semestreRepository.existsById(id)) {
            throw new RuntimeException("Semestre no encontrado con ID: " + id);
        }
        semestreRepository.deleteById(id);
    }


    private SemestreResponseDTO mapearAResponseDTO(Semestre s) {
        SemestreResponseDTO dto = new SemestreResponseDTO();
        dto.setIdSemestre(s.getIdSemestre());
        dto.setNombre(s.getNombre());
        dto.setIdEstado(s.getIdEstado());

        /* MAPEO INTELIGENTE: Si idEstado es 1L, el semestre esta ACTIVO. */ 
        /* Se calcula al vuelo para salvarle las papas al Frontend. */
        boolean esActivo = (s.getIdEstado() != null && s.getIdEstado() == 1L);
        dto.setActivo(esActivo);

        return dto;
    }

    private Semestre mapearAEntidad(SemestreRequestDTO dto) {
        Semestre s = new Semestre();
        s.setNombre(dto.getNombre());
        return s;
    }
}
