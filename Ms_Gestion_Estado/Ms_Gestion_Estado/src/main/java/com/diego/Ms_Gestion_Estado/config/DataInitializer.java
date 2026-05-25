package com.diego.Ms_Gestion_Estado.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.diego.Ms_Gestion_Estado.model.Estado;
import com.diego.Ms_Gestion_Estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EstadoRepository estadoRepository;

    @Override
    @Transactional
    public void run(String... run) {
        if (estadoRepository.count() > 0) {
            log.info("DataInitializer: Los estados ya están cargados en la BD.");
            return;
        }
        log.info("DataInitializer: BD vacía. Insertando estados por defecto...");
        try {
            estadoRepository.save(new Estado(null, "ACTIVO"));
            estadoRepository.save(new Estado(null, "INACTIVO"));
            estadoRepository.save(new Estado(null, "SUSPENDIDO"));
            log.info("DataInitializer: Estados cargados correctamente.");
        } catch (Exception e) {
            log.error("DataInitializer [ERROR]: Fallo al poblar los estados: {}", e.getMessage());
        }
    }
}
