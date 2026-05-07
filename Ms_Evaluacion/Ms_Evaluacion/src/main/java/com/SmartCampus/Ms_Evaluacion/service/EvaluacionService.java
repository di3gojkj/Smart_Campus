package com.SmartCampus.Ms_Evaluacion.service;

import org.springframework.stereotype.Service;

import com.SmartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.SmartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.SmartCampus.Ms_Evaluacion.model.Evaluacion;
import com.SmartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.SmartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.SmartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final TipoEvaluacionRepository tipoRepo;

    public EvaluacionResponseDTO guardar(EvaluacionRequestDTO dto){
        // Validacion existente de FK de tipo
        TipoEvaluacion tipo = tipoRepo.findById(dto.getIdTipoEvaluacion())
            .orElseThrow(() -> new RuntimeException
            ("Error: El tipo de evaluacion no existe"));

        //Mapeamos DTO a Identidad
        Evaluacion eval = new Evaluacion();

        eval.setNombre(dto.getNombre());
        eval.setDescripcion(dto.getDescripcion());
        eval.setTipoEvaluacion(tipo); //Relacion fisica ID_Tipo_Evaluacion

        Evaluacion guardada = evaluacionRepository.save(eval);

        // Aqui se implementa una respuesta limpia para el cliente

        return new EvaluacionResponseDTO(
            guardada.getIdEvaluacion(),
            guardada.getNombre(),
            guardada.getDescripcion(),
            tipo.getNombreTipo()

        );


    }

}
