package com.SmartCampus.Ms_Evaluacion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.SmartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.SmartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.SmartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.SmartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoEvaluacionService {

    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    public TipoEvaluacionResponseDTO guardar(TipoEvaluacionRequestDTO dto){
        TipoEvaluacion tipo = new TipoEvaluacion();
        tipo.setNombreTipo(dto.getNombreTipo().toUpperCase());
        TipoEvaluacion guardado = tipoEvaluacionRepository.save(tipo);
        return new TipoEvaluacionResponseDTO(guardado.getIdTipoEvaluacion(),
         guardado.getNombreTipo());

    }

    public List<TipoEvaluacionResponseDTO> listarTodos(){
        return tipoEvaluacionRepository.findAll().stream()
            .map(t -> new TipoEvaluacionResponseDTO(t.getIdTipoEvaluacion(), t.getNombreTipo()))
            .collect(Collectors.toList());
    }


}
