package com.smartcampus.msAsignatura.config;

import com.smartcampus.msAsignatura.model.Asignatura;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final SemestreRepository semestreRepository;
    private final AsignaturaRepository asignaturaRepository;

    // Inyección por constructor (nivel Senior)
    public DataInitializer(SemestreRepository semestreRepository, AsignaturaRepository asignaturaRepository) {
        this.semestreRepository = semestreRepository;
        this.asignaturaRepository = asignaturaRepository;
    }

    @Override
    public void run(String... args) {
        // Guardia: Si ya hay datos, no hacemos nada. ¡Protegemos tu BD!
        if (semestreRepository.count() > 0 || asignaturaRepository.count() > 0) {
            logger.info("DataInitializer: La base de datos ya contiene registros. Omitiendo carga.");
            return;
        }

        logger.info("DataInitializer: Iniciando carga de datos de prueba...");

        // 1. Cargamos Semestres
        // Usamos 1L para ACTIVO, 2L para otros estados (ej. Inactivo)
        semestreRepository.save(new Semestre(null, "2026-1", 1L));
        semestreRepository.save(new Semestre(null, "2025-2", 2L));
        
        logger.info("DataInitializer: Semestres inicializados correctamente.");

        // 2. Cargamos Asignaturas
        // El mapeo inteligente calculará 'activo' automáticamente al ver el '1L'
        asignaturaRepository.save(new Asignatura(null, "Desarrollo en Fullstack", "INF-401", 1L));
        asignaturaRepository.save(new Asignatura(null, "Base de Datos I", "INF-301", 1L));
        asignaturaRepository.save(new Asignatura(null, "Ingenieria en Software", "INF-405", 2L));

        logger.info("DataInitializer: Asignaturas inicializadas correctamente.");
        logger.info("DataInitializer: ¡Sistema listo para operar!");
    }
}
