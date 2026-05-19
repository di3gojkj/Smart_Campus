package com.SCampus.curso_seccion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{
    private final CursoRepository cursoRepository;

    @Override
    public void run(String... args){
        if (cursoRepository.count()>0) {
            log.info("Tabla Curso con datos precargados, se omite carga inicial");
            return;
        }
        cursoRepository.save(new Curso(null,"01/03/25"));
        cursoRepository.save(new Curso(null,"01/08/24"));
        cursoRepository.save(new Curso(null,"01/03/23"));
    }
    

}
