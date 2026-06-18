package com.smartCampus.Ms_Evaluacion.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.smartCampus.Ms_Evaluacion.model.Evaluacion;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!Control")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner{

    private final EvaluacionRepository evaluacionRepository;
    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    @Override
    public void run(String... args) throws Exception{
        log.info("[DataSeeder]: Verificando datos iniciales para el modulo de Evaluaciones...");

        if (tipoEvaluacionRepository.count()== 0) {

            log.info("Insertando Tipos de evaluacion iniciales...");

            TipoEvaluacion certamen = new TipoEvaluacion();
            certamen.setNombreTipo("Certamen");

            TipoEvaluacion Control = new TipoEvaluacion();
            Control.setNombreTipo("Control");

            TipoEvaluacion tarea = new TipoEvaluacion();
            tarea.setNombreTipo("Tarea/Proyecto");

            
            List<TipoEvaluacion> tiposGuardados = tipoEvaluacionRepository.saveAll(List.of(certamen, Control, tarea));
            log.info("[DataSeeder]: Tipos de evaluación cargados con éxito");
            
            if(evaluacionRepository.count()== 0){

                log.info("[DataSeeder]: Insertando Evaluaciones...");

                
                TipoEvaluacion tipoCertamen = tiposGuardados.stream()
                        .filter(t -> t.getNombreTipo().equals("Certamen"))
                        .findFirst().orElse(certamen);

                TipoEvaluacion tipoControl = tiposGuardados.stream()
                        .filter(t -> t.getNombreTipo().equals("Control"))
                        .findFirst().orElse(Control);

                Evaluacion e1 = new Evaluacion();
                e1.setNombre("Certamen 1");
                e1.setPorcentaje(25.0);
                e1.setTipoEvaluacion(tipoCertamen);

                Evaluacion e2 = new Evaluacion();
                e2.setNombre("Certamen 2");
                e2.setPorcentaje(30.0);
                e2.setTipoEvaluacion(tipoCertamen);

                Evaluacion e3 = new Evaluacion();
                e3.setNombre("Control de Entrada");
                e3.setPorcentaje(10.0);
                e3.setTipoEvaluacion(tipoControl);

                evaluacionRepository.saveAll(List.of(e1, e2, e3));
                log.info("[DataSeeder]: Evaluaciones iniciales cargadas con éxito");
            }

        }else{

            log.info("[DataSeeder]: La base de datos ya contiene datos de evaluaciones, omitiendo carga inicial");

        }
    }
}
