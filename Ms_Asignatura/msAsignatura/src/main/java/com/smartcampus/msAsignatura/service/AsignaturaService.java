package com.smartcampus.msAsignatura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.model.Asignatura;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    
    private AsignaturaResponseDTO mapToDto(Asignatura a){
        return new AsignaturaResponseDTO(
            a.getId_Asignatura(),
            a.getSigla(),
            a.getNombre_asignatura(),
            a.getIdEstado()
        );
    }

    public List<AsignaturaResponseDTO> obtenerTodos(){
        return asignaturaRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
    }

    public AsignaturaResponseDTO guardar(AsignaturaRequestDTO dto) {
        try {
            if (asignaturaRepository.findBySiglaIgnoreCase(dto.getSigla()).isPresent()) {
                    throw new RuntimeException("Ya existe la sigla: " + dto.getSigla());
            }

            Asignatura a = new Asignatura();
            a.setSigla(dto.getSigla().toUpperCase());
            a.setNombre_asignatura(dto.getNombre().toUpperCase());
            a.setIdEstado(dto.getIdEstado());                asignaturaRepository.save(a);
             return mapToDto(asignaturaRepository.save(a));
        } catch (Exception e) {
            log.error("Error al guardar asignatura: {}", e.getMessage());
            throw new RuntimeException("Error técnico: " + e.getMessage());          
        }
    }

}
