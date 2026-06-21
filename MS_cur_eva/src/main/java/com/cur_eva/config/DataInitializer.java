package com.cur_eva.config;

import org.springframework.boot.CommandLineRunner; // IMPORTACIÓN OBLIGATORIA
import org.springframework.stereotype.Component;

import com.cur_eva.repository.CursoEvaluacionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner { // Implementa el runner oficial

    private final CursoEvaluacionRepository cursoEvaluacionRepository;

    @Override
    public void run(String... args) throws Exception {
        
        log.info("Verificando estado inicial de la base de datos de evaluaciones (cur_eva)...");
        
        long registros = cursoEvaluacionRepository.count();
        log.info("Cantidad de registros encontrados en la tabla estados: {}", registros);
    }
}

