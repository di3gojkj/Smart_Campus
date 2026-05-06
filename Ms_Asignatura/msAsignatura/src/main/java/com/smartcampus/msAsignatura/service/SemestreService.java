package com.smartcampus.msAsignatura.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SemestreService {

    private final SemestreRepository semestreRepository;


    private SemestreResponseDTO mapToDto(Semestre s){
        return new SemestreResponseDTO(
            s.getIdSemestre(),
            s.getNombre_semestre()
        );
    }

    public List<SemestreResponseDTO> obtenerTodos(){
        return semestreRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
    }

    public SemestreResponseDTO guardar(SemestreRequestDTO dto) { 
        try {
            Semestre s = new Semestre();
            s.setNombre_semestre(dto.getNombre_semestre().toUpperCase());
            return mapToDto(semestreRepository.save(s));
        } catch (Exception e) {
            log.error("Error al guardar: {}", e.getMessage());
            throw new RuntimeException("Error al crear semestre");
        }
    }
}
