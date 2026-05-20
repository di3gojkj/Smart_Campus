package com.diego.Ms_Gestion_Lista.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.diego.Ms_Gestion_Lista.repository.ListaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ListaRepository listaRepository;

    @Override
    public void run(String... args) {
        log.info("Inicializando verificaciones de entorno de aula...");
        // Dejado listo para monitorear consistencia de la base de datos distribuida
        log.info("Entorno listo. Registros de clases activos en el sistema: {}", listaRepository.count());
    }

}
