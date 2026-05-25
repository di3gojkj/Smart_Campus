package com.SCampus.curso_seccion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CursoRepository cursoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (cursoRepository.count() > 0) {
            log.info("DataInitializer: Tabla de Cursos contiene registros. Omitiendo populado.");
            return;
        }
        log.info("DataInitializer: Tabla de Cursos vacía. Registrando semestres por defecto...");
        try {
            cursoRepository.save(new Curso(null, "01/03/25"));
            cursoRepository.save(new Curso(null, "01/08/24"));
            cursoRepository.save(new Curso(null, "01/03/23"));
            log.info("DataInitializer: Periodos cargados exitosamente de forma persistente.");
        } catch (Exception e) {
            log.error("DataInitializer [ERROR CRÍTICO]: Falló la carga inicial de cursos: {}", e.getMessage());
        }
    }
}
