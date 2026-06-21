package com.diego.MS_Gestion_Usuario.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.diego.MS_Gestion_Usuario.model.Rol;
import com.diego.MS_Gestion_Usuario.model.Usuario;
import com.diego.MS_Gestion_Usuario.repository.RolRepository;
import com.diego.MS_Gestion_Usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    
    
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        
        if (rolRepository.count() > 0) {
            log.info("DataInitializer: Estructura de roles ya detectada en la BD. Omitiendo populado.");
        } else {
            log.info("DataInitializer: Base de datos de roles vacía. Iniciando inserción de roles universitarios...");
            try {
                rolRepository.save(new Rol(null, "ADMINISTRADOR", 1L));
                rolRepository.save(new Rol(null, "DOCENTE", 1L));
                rolRepository.save(new Rol(null, "ESTUDIANTE", 1L));
                log.info("Roles cargados exitosamente. Total registros: {}", rolRepository.count());
            } catch (Exception e) {
                log.error("No se pudieron inicializar los roles: {}", e.getMessage());
            }
        }

    
        if (usuarioRepository.count() > 0) {
            log.info("Estructura de usuarios ya detectada en la BD. Omitiendo populado.");
        } else {
            log.info("Base de datos de usuarios vacía. Iniciando inserción de cuentas maestras...");
            try {
                Usuario admin = new Usuario(
                        null, 
                        "15421518-1", 
                        "Diego", 
                        "Administrador", 
                        "admin@duocuc.cl", 
                        passwordEncoder.encode("admin123"), 
                        1L, 
                        new HashSet<>(List.of(rolRepository.findById(1L).orElseThrow()))
                );

                Usuario estudiante = new Usuario(
                        null, 
                        "12345658-2", 
                        "Juanito", 
                        "Pérez", 
                        "juan.perez@duocuc.cl", 
                        passwordEncoder.encode("estudiante123"), 
                        1L, 
                        new HashSet<>(List.of(rolRepository.findById(3L).orElseThrow()))
                );

                usuarioRepository.save(admin);
                usuarioRepository.save(estudiante);
                log.info("Usuarios cargados exitosamente. Total registros: {}", usuarioRepository.count());
            } catch (Exception e) {
                log.error("No se pudieron inicializar los usuarios: {}", e.getMessage());
            }
        }
    }
}