package com.cur_eva.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import feign.FeignException; 

import com.cur_eva.client.EvaluacionClient; 
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoEvaluacionService {
    
    private final CursoEvaluacionRepository cursoEvaluacionRepository;
    private final EvaluacionClient evaluacionClient; 
    
    // Mapeo de entidad a DTO
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
                .orElseThrow();
        return mapToDTO(cursoEvaluacion);
    }

    public CursoEvaluacionResponseDTO guardar(CursoEvaluacionRequestDTO dto){
        
        if (cursoEvaluacionRepository.findByNombreIgnoreCase(dto.getNombre()).isPresent()){
            throw new RuntimeException("Ya existe un CursoEvaluacion con el nombre: " + dto.getNombre());
        }

        
        if (dto.getIdEvaluacion() != null) {
            try {
                
                evaluacionClient.buscarPorId(dto.getIdEvaluacion());
            } catch (FeignException.NotFound e) {
               
                throw new RuntimeException("Error de Consistencia: La evaluación con ID " + dto.getIdEvaluacion() + " no existe en el sistema remoto.");
            } catch (FeignException e) {
                
                throw new RuntimeException("El servicio de evaluaciones no se encuentra disponible temporalmente.");
            }
        }

        
        CursoEvaluacion cursoEvaluacion = new CursoEvaluacion(null, dto.getNombre().toUpperCase(), null, null, null);
        return mapToDTO(cursoEvaluacionRepository.save(cursoEvaluacion));
    }
}
