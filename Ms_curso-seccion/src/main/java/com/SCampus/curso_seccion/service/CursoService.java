package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);
    private final CursoRepository cursoRepository;

    private CursoResponseDTO mapToDTO(Curso c) {
        return new CursoResponseDTO(c.getId(), c.getFechaCreacion());
    }

    @Transactional(readOnly = true)
    public List<CursoResponseDTO> obtenerTodos() {
        logger.info("Consultando la lista completa de cursos académicos de la institución");
        return cursoRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public CursoResponseDTO guardarCurso(CursoResponseDTO dto) {
        logger.info("Intentando registrar un nuevo periodo de curso académico: {}", dto.getFechaCreacion());
        
        if (cursoRepository.findByFechaCreacion(dto.getFechaCreacion()).isPresent()) {
            logger.warn("Rechazo de persistencia: El curso con fecha {} ya se encuentra registrado", dto.getFechaCreacion());
            throw new RuntimeException("Conflicto Académico: El curso con esa fecha ya existe.");
        }

        Curso curso = new Curso(null, dto.getFechaCreacion());
        Curso guardado = cursoRepository.save(curso);
        logger.info("Curso guardado exitosamente en la BD local con ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }
}
