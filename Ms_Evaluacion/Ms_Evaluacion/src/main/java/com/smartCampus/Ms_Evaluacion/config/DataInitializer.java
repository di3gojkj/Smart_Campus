package com.smartCampus.Ms_Evaluacion.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smartCampus.Ms_Evaluacion.model.Evaluacion;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EvaluacionRepository evaluacionRepository;
    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD...");

        if (tipoEvaluacionRepository.count() == 0) {
            log.info("[DataInitializer]: Tipo Evaluaciones y Evaluaciones no encontradas. Insertando datos iniciales..");

            
            TipoEvaluacion certamen = new TipoEvaluacion();
            certamen.setNombreTipo("Certamen");
            certamen = tipoEvaluacionRepository.save(certamen);

            TipoEvaluacion control = new TipoEvaluacion();
            control.setNombreTipo("Control");
            control = tipoEvaluacionRepository.save(control);

            TipoEvaluacion tarea = new TipoEvaluacion();
            tarea.setNombreTipo("Tarea/Proyecto");
            tarea = tipoEvaluacionRepository.save(tarea);

            Evaluacion e1 = new Evaluacion();
            e1.setNombre("Certamen 1");
            e1.setPorcentaje(25.0);
            e1.setTipoEvaluacion(certamen);

            Evaluacion e2 = new Evaluacion();
            e2.setNombre("Certamen 2");
            e2.setPorcentaje(30.0);
            e2.setTipoEvaluacion(certamen);

            Evaluacion e3 = new Evaluacion();
            e3.setNombre("Control de Entrada");
            e3.setPorcentaje(10.0);
            e3.setTipoEvaluacion(control);

            evaluacionRepository.saveAll(List.of(e1, e2, e3));

            log.info("[DataInitializer]: Datos iniciales cargados correctamente");
        } else {
            log.info("[DataInitializer]: La tabla Tipo Evaluacion y Evaluacion ya contienen datos, omitiendo carga...");
        }
    }

}
