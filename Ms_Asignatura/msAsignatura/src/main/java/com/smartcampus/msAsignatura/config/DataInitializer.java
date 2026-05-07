package com.smartcampus.msAsignatura.config;

import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartcampus.msAsignatura.service.SemestreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AsignaturaRepository asignaturaRepository;
    private final SemestreService semestreService;


    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando datos de prueba...");

        if (semestreService.obtenerTodos().isEmpty()) {
            log.info("Cargando semestres por defecto...");
            
            SemestreRequestDTO sem1 = new SemestreRequestDTO();
            sem1.setNombre_semestre("PRIMER SEMESTRE 2026");
            semestreService.guardar(sem1);

            SemestreRequestDTO sem2 = new SemestreRequestDTO();
            sem2.setNombre_semestre("SEGUNDO SEMESTRE 2026");
            semestreService.guardar(sem2);
        } 

        if (asignaturaRepository.count()== 0) {
            log.info("La tabla de Asignaturas está lista y vacía para nuevos registros.");
        }
    }
}
