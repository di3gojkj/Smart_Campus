package com.smartCampus.Ms_Evaluacion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final TipoEvaluacionRepository tipoEvaluacionRepository;

    public DataInitializer(TipoEvaluacionRepository tipoEvaluacionRepository) {
        this.tipoEvaluacionRepository = tipoEvaluacionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Verificando datos iniciales en la base de datos...");

        if (tipoEvaluacionRepository.count() == 0) {
            logger.info("Base de datos vacía. Iniciando carga de datos iniciales...");

            TipoEvaluacion t1 = new TipoEvaluacion();
            t1.setNombre("Parcial");

            TipoEvaluacion t2 = new TipoEvaluacion();
            t2.setNombre("Control");

            TipoEvaluacion t3 = new TipoEvaluacion();
            t3.setNombre("Proyecto");

            tipoEvaluacionRepository.saveAll(List.of(t1, t2, t3));
            
            logger.info("Datos iniciales cargados correctamente: Parcial, Control, Proyecto.");
        } else {
            logger.info("La base de datos ya contiene registros. Saltando inicialización.");
        }
    }
}
