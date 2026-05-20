package com.diego.MS_Gestion_Usuario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.diego.MS_Gestion_Usuario.model.Rol;
import com.diego.MS_Gestion_Usuario.repository.RolRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        if (rolRepository.count() > 0) {
            log.info("Estructura de roles ya poblada.");
            return;
        }

        log.info("Poblado inicial de roles universitarios en progreso...");
        try {
            // Asumimos lógicamente que el idEstado '1' corresponde a 'ACTIVO' en la red
            rolRepository.save(new Rol(null, "ADMINISTRADOR", 1L));
            rolRepository.save(new Rol(null, "DOCENTE", 1L));
            rolRepository.save(new Rol(null, "ESTUDIANTE", 1L));
            log.info("Carga de roles finalizada. Total: {}", rolRepository.count());
        } catch (Exception e) {
            log.error("Error cargando listas de roles: {}", e.getMessage());
        }
    }
}
