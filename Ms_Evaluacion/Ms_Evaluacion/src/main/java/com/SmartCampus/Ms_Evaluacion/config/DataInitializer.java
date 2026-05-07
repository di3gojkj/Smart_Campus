package com.SmartCampus.Ms_Evaluacion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.SmartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.SmartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j

public class DataInitializer implements CommandLineRunner {

    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    @Override
    public void run(String ... args){
        if(tipoEvaluacionRepository.count() == 0){
            log.info("Iniciando carga de datos maestros para Evaluacion...");

            tipoEvaluacionRepository.save(new TipoEvaluacion(null, "CERTAMEN"));
            tipoEvaluacionRepository.save(new TipoEvaluacion(null, "TAREA"));
            tipoEvaluacionRepository.save(new TipoEvaluacion(null, "CONTROL"));
            tipoEvaluacionRepository.save(new TipoEvaluacion(null, "EXAMEN"));
            
            log.info("Carga de datos de Evaluacion finalizada con exito.");

        }
    }
}
