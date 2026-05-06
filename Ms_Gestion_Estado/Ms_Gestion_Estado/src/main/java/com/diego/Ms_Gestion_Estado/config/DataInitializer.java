package com.diego.Ms_Gestion_Estado.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.diego.Ms_Gestion_Estado.model.Estado;
import com.diego.Ms_Gestion_Estado.repository.EstadoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{

    private final EstadoRepository estadoRepository;

    @Override
    public void run(String... run){
        if (estadoRepository.count() > 0){
            log.info("Los estados ya estan cargados en la BD");
            return;
        }
        log.info("Insertando estados por defecto...");
        estadoRepository.save(new Estado(null, "ACTIVO"));
        estadoRepository.save(new Estado(null, "INACTIVO"));
        estadoRepository.save(new Estado(null, "SUSPENDIDO"));
        log.info("Estados cargados coorectamente.");
    }
}
