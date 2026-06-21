package com.diego.Ms_Gestion_Lista.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.diego.Ms_Gestion_Lista.model.Calificacion;
import com.diego.Ms_Gestion_Lista.model.Lista;
import com.diego.Ms_Gestion_Lista.repository.CalificacionRepository;
import com.diego.Ms_Gestion_Lista.repository.ListaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ListaRepository listaRepository;
    private final CalificacionRepository calificacionRepository; 

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("DataInitializer: Inicializando verificaciones de entorno de aula...");
            
            if (listaRepository.count() > 0) {
                log.info("DataInitializer: Entorno listo. Registros de clases activos en el sistema: {}", listaRepository.count());
            } else {
                log.info("DataInitializer: Base de datos vacía. Insertando inscripciones y calificaciones de prueba...");
                
           
                Lista lista1 = new Lista(null, 2L, 1L, LocalDateTime.now());
                Lista lista2 = new Lista(null, 2L, 2L, LocalDateTime.now());

        
                listaRepository.save(lista1);
                listaRepository.save(lista2);
                log.info("Listas (Inscripciones) creadas exitosamente.");

            
   
                Calificacion nota1 = new Calificacion(null, new BigDecimal("6.5"), lista1, 1L);
                Calificacion nota2 = new Calificacion(null, new BigDecimal("5.8"), lista2, 1L);
                

                calificacionRepository.save(nota1);
                calificacionRepository.save(nota2);
                
                log.info("3 Calificaciones creadas y asociadas exitosamente a los registros.");
            }
        } catch (Exception e) {
            log.error("Fallo en la comprobación inicial o carga de datos: {}", e.getMessage());
        }
    }
}