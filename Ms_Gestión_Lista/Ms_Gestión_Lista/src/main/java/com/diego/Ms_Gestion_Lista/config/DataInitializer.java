package com.diego.Ms_Gestion_Lista.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.diego.Ms_Gestion_Lista.repository.ListaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ListaRepository listaRepository;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("DataInitializer: Inicializando verificaciones de entorno de aula...");
            log.info("DataInitializer: Entorno listo. Registros de clases activos en el sistema: {}", listaRepository.count());
        } catch (Exception e) {
            log.error("DataInitializer [ERROR]: Fallo en la comprobación inicial: {}", e.getMessage());
        }
    }
}
