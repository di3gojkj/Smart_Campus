package com.smartCampus.Ms_Carrera.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CarreraRepository carreraRepository;
    private final CarreraAsignaturaRepository carreraAsignaturaRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD...");
        if (carreraRepository.count() == 0) {
            log.info("[DataInitializer]: Carrera y Relaciones no encontradas. Insertando datos iniciales..");

            
            Carrera carrera1 = carreraRepository.save(new Carrera(null, "Ingenieria en Informatica", "INF-001", 1L));
            Carrera carrera2 = carreraRepository.save(new Carrera(null, "Mecanica Automotriz", "MCA-001", 1L));

            
            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera1, 1L, 1L));
            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera1, 2L, 1L));
            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera2, 3L, 1L));

            log.info("[DataInitializer]: Datos iniciales cargados correctamente");
        } else {
            log.info("[DataInitializer]: La tabla Carrera y Carrera-Asig ya contiene datos, omitiendo carga...");
        }
    }

}
