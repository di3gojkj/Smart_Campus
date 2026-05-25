package com.diego.MS_Gestion_Usuario.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.diego.MS_Gestion_Usuario.model.Rol;
import com.diego.MS_Gestion_Usuario.repository.RolRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RolRepository rolRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (rolRepository.count() > 0) {
            logger.info("DataInitializer: Estructura de roles ya detectada en la BD. Omitiendo populado.");
            return;
        }

        logger.info("DataInitializer: Base de datos vacía. Iniciando inserción de roles universitarios...");
        try {
            // Se asume el ID referencial 1L para vincular con el estado 'ACTIVO' del otro MS
            rolRepository.save(new Rol(null, "ADMINISTRADOR", 1L));
            rolRepository.save(new Rol(null, "DOCENTE", 1L));
            rolRepository.save(new Rol(null, "ESTUDIANTE", 1L));
            logger.info("DataInitializer: Roles cargados exitosamente. Total registros: {}", rolRepository.count());
        } catch (Exception e) {
            logger.error("DataInitializer [ERROR CRÍTICO]: No se pudieron inicializar los roles: {}", e.getMessage());
        }
    }
}