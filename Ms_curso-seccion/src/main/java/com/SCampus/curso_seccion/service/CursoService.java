package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.stream.Collector;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final RestTemplate restTemplate;

    //mapeo del DTO
    private CursoResponseDTO mapDTO(Curso curso){
        return new CursoResponseDTO(
            curso.getId(),
            curso.getFechaCreacion()
        );
    }

    //logica para crear, modificar, eliminar o buscar
    //buscar todos los cursos
    public List<CursoResponseDTO> obtenerTodos(){
        return cursoRepository.findAll().stream().map(this::mapDTO).collect(Collector.toList()); 
    }
    //Agregar nuevo curso
    public CursoResponseDTO guardarCurso(CursoResponseDTO curs){
        Curso curso = new Curso(null, curs.getFechaCreacion());
        return mapDTO(curso);
    }


}
