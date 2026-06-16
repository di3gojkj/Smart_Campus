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
    public List<SemestreResponseDTO> listarTodosCronologicos() {
        return semestreRepository.listarSemestreCronologicos().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SemestreResponseDTO buscarPorId(Long id) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado con ID: " + id));
        return mapearAResponseDTO(semestre);
    }

    @Transactional
    public SemestreResponseDTO crear(SemestreRequestDTO dto) {
        if (semestreRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()) {
            throw new RuntimeException("El semestre '" + dto.getNombre() + "' ya se encuentra registrado");
        }

        Semestre nuevo = mapearAEntidad(dto);
        Semestre guardado = semestreRepository.save(nuevo);
        logger.info("Semestre registrado exitosamente con ID: {}", guardado.getIdSemestre());
        return mapearAResponseDTO(guardado);
    }

    @Transactional
    public SemestreResponseDTO actualizar(Long id, SemestreRequestDTO dto) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre no encontrado con ID: " + id));

        semestre.setNombre(dto.getNombre());
        semestre.setIdEstado(dto.getIdEstado());

        Semestre actualizado = semestreRepository.save(semestre);
        logger.info("Semestre ID: {} actualizado correctamente", id);
        return mapearAResponseDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!semestreRepository.existsById(id)) {
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

        dto.setActivo(s.getIdEstado() != null && s.getIdEstado() == 1L);

        
        if (s.getIdEstado() != null) {
            EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(s.getIdEstado());
            dto.setNombreEstado(estado.getNombre());
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
