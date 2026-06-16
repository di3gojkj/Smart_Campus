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

    public AsignaturaService(AsignaturaRepository asignaturaRepository, EstadoClient estadoClient) {
        this.asignaturaRepository = asignaturaRepository;
        this.estadoClient = estadoClient;
    }

    @Transactional(readOnly = true)
    public List<AsignaturaResponseDTO> listarTodas() {
        return asignaturaRepository.findAll().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsignaturaResponseDTO buscarPorId(Long id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + id));
        return mapearAResponseDTO(asignatura);
    }

    @Transactional
    public AsignaturaResponseDTO crear(AsignaturaRequestDTO dto) {
        if (asignaturaRepository.findBySiglaIgnoreCase(dto.getSigla()).isPresent()) {
            throw new RuntimeException("La sigla '" + dto.getSigla() + "' ya pertenece a otra asignatura");
        }

        Asignatura nueva = mapearAEntidad(dto);
        Asignatura guardada = asignaturaRepository.save(nueva);
        logger.info("Asignatura registrada exitosamente con ID: {}", guardada.getIdAsignatura());
        return mapearAResponseDTO(guardada);
    }

    @Transactional
    public AsignaturaResponseDTO actualizar(Long id, AsignaturaRequestDTO dto) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con ID: " + id));

        asignatura.setNombre(dto.getNombre());
        asignatura.setSigla(dto.getSigla());
        asignatura.setIdEstado(dto.getIdEstado());

        Asignatura actualizada = asignaturaRepository.save(asignatura);
        logger.info("Asignatura ID: {} actualizada correctamente", id);
        return mapearAResponseDTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!asignaturaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Asignatura no encontrada con ID: " + id);
        }
        asignaturaRepository.deleteById(id);
        logger.info("Asignatura ID: {} eliminada correctamente", id);
    }

    private AsignaturaResponseDTO mapearAResponseDTO(Asignatura a) {
        AsignaturaResponseDTO dto = new AsignaturaResponseDTO();
        dto.setIdAsignatura(a.getIdAsignatura());
        dto.setNombre(a.getNombre());
        dto.setSigla(a.getSigla());
        dto.setIdEstado(a.getIdEstado());

        dto.setActivo(a.getIdEstado() != null && a.getIdEstado() == 1L);

        
        if (a.getIdEstado() != null) {
            EstadoResponseDTO estado = estadoClient.obtenerEstadoPorId(a.getIdEstado());
            dto.setNombreEstado(estado.getNombre());
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
