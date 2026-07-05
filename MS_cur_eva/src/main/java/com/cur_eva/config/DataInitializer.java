package com.cur_eva.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CursoEvaluacionRepository cursoEvaluacionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD para cur_eva...");
        if (cursoEvaluacionRepository.count() == 0) {
            log.info("[DataInitializer]: Evaluaciones de cursos no encontradas. Insertando datos iniciales...");

            CursoEvaluacion estado1 = new CursoEvaluacion();
            estado1.setNombre("ACTIVO");
            estado1.setIdCurso(12L);
            estado1.setIdEvaluacion(100L);
            estado1.setFCreacion("2026-06-21");
            estado1.setFApertura("2026-06-20");
            estado1.setFCierre("2026-07-20");
            cursoEvaluacionRepository.save(estado1);

            CursoEvaluacion estado2 = new CursoEvaluacion();
            estado2.setNombre("PENDIENTE");
            estado2.setIdCurso(12L);
            estado2.setIdEvaluacion(101L);
            estado2.setFCreacion("2026-06-21");
            estado2.setFApertura("2026-07-21");
            estado2.setFCierre("2026-08-21");
            cursoEvaluacionRepository.save(estado2);

            log.info("[DataInitializer]: Datos iniciales cargados correctamente en cur_eva");
        } else {
            log.info("[DataInitializer]: La tabla de evaluaciones ya contiene datos, omitiendo carga...");
        }
    }
}

