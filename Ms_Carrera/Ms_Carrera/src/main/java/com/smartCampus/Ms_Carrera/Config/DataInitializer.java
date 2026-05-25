package com.smartCampus.Ms_Carrera.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRespository;
import com.smartCampus.Ms_Carrera.model.Carrera;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CarreraAsignaturaRepository caRepository;
    private final CarreraRespository carreraRepository;

    public DataInitializer(CarreraAsignaturaRepository caRepository, CarreraRespository carreraRepository) {
        this.caRepository = caRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public void run(String... args) {
        // Guardia: Si ya hay datos, lo informamos y salimos.
        if (caRepository.count() > 0) {
            logger.info("DataInitializer: La base de datos de Carrera ya contiene registros. Omitiendo carga.");
            return;
        }

        logger.info("DataInitializer: Iniciando carga de datos de prueba para Carrera...");

        // 1. Crear una Carrera de prueba
        Carrera c1 = new Carrera();
        c1.setNombre("Ingeniería en Informática");
        c1.setSigla("INF-001");
        c1.setIdEstado(1L);
        carreraRepository.save(c1);
        
        logger.info("DataInitializer: Carrera creada: {}", c1.getNombre());

        // 2. Crear relaciones
        crearRelacion(c1, 101L, 1L);
        crearRelacion(c1, 102L, 1L);
        crearRelacion(c1, 103L, 2L);

        logger.info("DataInitializer: Relaciones de Carrera-Asignatura cargadas exitosamente.");
        logger.info("DataInitializer: ¡Sistema de Carrera listo para operar!");
    }

    private void crearRelacion(Carrera carrera, Long idAsig, Long idSem) {
        // Asumiendo que tu modelo CarreraAsignatura tiene un constructor para esto
        // O setter methods como tenías en tu código original
        var ca = new com.smartCampus.Ms_Carrera.model.CarreraAsignatura();
        ca.setCarrera(carrera);
        ca.setIdAsignatura(idAsig);
        ca.setIdSemestre(idSem);
        caRepository.save(ca);
    }

}
