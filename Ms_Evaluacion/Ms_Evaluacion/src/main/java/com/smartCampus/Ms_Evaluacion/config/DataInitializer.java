package com.smartCampus.Ms_Evaluacion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartCampus.Ms_Evaluacion.model.Evaluacion;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final TipoEvaluacionRepository tipoEvaluacionRepository;
    private final EvaluacionRepository evaluacionRepository;

    // Inyección por constructor: Estándar de la industria
    public DataInitializer(TipoEvaluacionRepository tipoEvaluacionRepository, EvaluacionRepository evaluacionRepository) {
        this.tipoEvaluacionRepository = tipoEvaluacionRepository;
        this.evaluacionRepository = evaluacionRepository;
    }

    @Override
    public void run(String... args) {
        // Guardia: Si la BD ya tiene datos, no hacemos nada para evitar duplicados
        if (tipoEvaluacionRepository.count() > 0) {
            logger.info("DataInitializer: La base de datos de Evaluación ya contiene registros. Omitiendo carga.");
            return;
        }

        logger.info("DataInitializer: Iniciando carga de datos de prueba para Evaluaciones...");

        // 1. Guardamos tipos y capturamos las instancias (t1, t2, t3) 
        // Pasamos 'null' en la lista de evaluaciones porque al crear el tipo aún no tiene hijos.
        // Nota: Si usas @AllArgsConstructor de Lombok, el orden es: ID, Nombre, Lista
        TipoEvaluacion t1 = tipoEvaluacionRepository.save(new TipoEvaluacion(null, "Parcial", null));
        TipoEvaluacion t2 = tipoEvaluacionRepository.save(new TipoEvaluacion(null, "Control", null));
        TipoEvaluacion t3 = tipoEvaluacionRepository.save(new TipoEvaluacion(null, "Proyecto", null));

        logger.info("DataInitializer: Tipos de evaluación inicializados.");

        // 2. Creamos Evaluaciones usando las instancias guardadas previamente
        evaluacionRepository.save(new Evaluacion(null, "Primer Parcial", 30.0, t1));
        evaluacionRepository.save(new Evaluacion(null, "Control de Lectura", 10.0, t2));
        evaluacionRepository.save(new Evaluacion(null, "Proyecto Final", 40.0, t3));

        logger.info("DataInitializer: Evaluaciones de prueba cargadas correctamente.");
        logger.info("DataInitializer: ¡Sistema de Evaluación listo para operar!");
    }
}
