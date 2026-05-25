package com.cur_eva.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.exception.CursoEvaluacionNotFoundException;
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoEvaluacionService {
    private final CursoEvaluacionRepository cursoEvaluacionRepository;
    
    //Mapeo de entidad a DTO
    private CursoEvaluacionResponseDTO mapToDTO(CursoEvaluacion c){
        return new CursoEvaluacionResponseDTO(c.getIdCursoEvaluacion(), c.getNombre(), c.getFApertura(), c.getFCierre(), c.getFApertura());
    }

    public List<CursoEvaluacionResponseDTO> obtenerTodos(){
        return cursoEvaluacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CursoEvaluacionResponseDTO obtenerPorId(Long id) {
        CursoEvaluacion cursoEvaluacion = cursoEvaluacionRepository.findById(id)
                .orElseThrow(() -> new CursoEvaluacionNotFoundException(id));
        return mapToDTO(cursoEvaluacion);
    }

    public CursoEvaluacionResponseDTO guardar(CursoEvaluacionRequestDTO dto){
        if (cursoEvaluacionRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()){//Antes de guardar, va a la base de datos a preguntar si ya existe ese nombre. Si existe, lanza un error para no tener la base de datos sucia.
            throw new RuntimeException("Ya existe un CursoEvaluacion con el nombre: " + dto.getNombre());
        }
        CursoEvaluacion cursoEvaluacion = new CursoEvaluacion(null, dto.getNombre().toUpperCase(), null, null, null);
        return mapToDTO(cursoEvaluacionRepository.save(cursoEvaluacion));
    }
}
