package com.SCampus.curso_seccion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.repository.CursoRepository;
import com.SCampus.curso_seccion.repository.SeccionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD para curso-seccion...");
        if (cursoRepository.count() == 0) {
            log.info("[DataInitializer]: Estructura académica no encontrada. Insertando datos iniciales...");

            Curso curso1 = cursoRepository.save(new Curso(null, "Programación Orientada a Objetos", "14/06/26"));
            Curso curso2 = cursoRepository.save(new Curso(null, "Estructuras de Datos", "20/06/26"));

            Seccion seccion1 = new Seccion();
            seccion1.setNombre("Sección Alpha");
            seccion1.setCurso(curso1);
            seccionRepository.save(seccion1);

            Seccion seccion2 = new Seccion();
            seccion2.setNombre("Sección Beta");
            seccion2.setCurso(curso1);
            seccionRepository.save(seccion2);

            Seccion seccion3 = new Seccion();
            seccion3.setNombre("Matutina");
            seccion3.setCurso(curso2);
            seccionRepository.save(seccion3);

            log.info("[DataInitializer]: Datos iniciales cargados correctamente en curso-seccion");
        } else {
            log.info("[DataInitializer]: La tabla de cursos ya contiene datos, omitiendo carga...");
        }
    }
}
