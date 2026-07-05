package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoService {

    private final CursoRepository cursoRepository;

    private CursoResponseDTO mapToDTO(Curso c) {
        CursoResponseDTO dto = new CursoResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setFechaCreacion(c.getFechaCreacion());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDTO> obtenerTodos() {
        log.info("Consultando la lista completa de cursos académicos de la institución");
        return cursoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CursoResponseDTO guardarCurso(CursoRequestDTO dto) {
        log.info("Intentando registrar un nuevo periodo de curso académico: {}", dto.getNombre());
        
        if (cursoRepository.findByFechaCreacion(dto.getFechaCreacion()).isPresent()) {
            log.warn("Rechazo de persistencia: El curso con fecha {} ya se encuentra registrado", dto.getFechaCreacion());
            throw new RuntimeException("Conflicto Académico: El curso con esa fecha ya existe.");
        }

        Curso curso = new Curso();
        curso.setNombre(dto.getNombre());
        curso.setFechaCreacion(dto.getFechaCreacion());
        
        Curso guardado = cursoRepository.save(curso);
        log.info("Curso guardado exitosamente en la BD local con ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    @Transactional(readOnly = true)
    public Optional<CursoResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando curso por ID: {}", id);
        return cursoRepository.findById(id).map(this::mapToDTO);
    }
}
