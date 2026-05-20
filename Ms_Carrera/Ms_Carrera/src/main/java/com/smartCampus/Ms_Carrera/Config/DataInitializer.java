package com.smartCampus.Ms_Carrera.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRespository;
import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CarreraAsignaturaRepository caRepository;
    private final CarreraRespository carreraRepository;

    public DataInitializer(CarreraAsignaturaRepository caRepository, CarreraRespository carreraRepository) {
        this.caRepository = caRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Solo cargamos si está vacío para no duplicar datos
        if (caRepository.count() == 0) {
            logger.info("Iniciando carga de datos de prueba...");

            // 1. Crear una Carrera de prueba
            Carrera c1 = new Carrera();
            c1.setNombre("Ingeniería en Informática");
            c1.setSigla("INF-001");
            carreraRepository.save(c1);

            // 2. Crear relaciones (Simulando IDs de Asignaturas/Semestres externos)
            // Asumimos IDs 101, 102 de Asignatura y 1, 2 de Semestre (que existen en tu otro MS)
            crearRelacion(c1, 101L, 1L);
            crearRelacion(c1, 102L, 1L);
            crearRelacion(c1, 103L, 2L);

            logger.info("Datos de prueba cargados exitosamente.");
        }
    }

    private void crearRelacion(Carrera carrera, Long idAsig, Long idSem) {
        CarreraAsignatura ca = new CarreraAsignatura();
        ca.setCarrera(carrera);
        ca.setIdAsignatura(idAsig);
        ca.setIdSemestre(idSem);
        caRepository.save(ca);
    }

}
