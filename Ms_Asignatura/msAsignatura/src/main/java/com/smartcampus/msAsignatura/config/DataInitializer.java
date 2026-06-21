package com.smartcampus.msAsignatura.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.smartcampus.msAsignatura.model.Asignatura;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner{

    private final AsignaturaRepository asignaturaRepository;
    private final SemestreRepository semestreRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("[DataInitializer]: Verificando estado de la BD...");
        

        
        if (semestreRepository.count() == 0) {
            log.info("[DataInitializer]: Semestres no encontrados. Insertando datos iniciales...");
            Semestre s1 = new Semestre();
            s1.setNombre("2026-1");
            s1.setIdEstado(1L); 

            Semestre s2 = new Semestre();
            s2.setNombre("2026-2");
            s2.setIdEstado(1L);

            semestreRepository.saveAll(List.of(s1, s2));
            log.info("[DataInitializer]: Datos iniciales de semestre cargados con exito");
        } else {
            log.info("[DataInitializer]: La tabla Semestre ya contiene datos. Omitiendo carga...");
        }

        
        if (asignaturaRepository.count() == 0) {
            log.info("[DataInitializer]: Asignaturas no encontradas. Insertando datos iniciales...");

            Asignatura a1 = new Asignatura();
            a1.setNombre("Base de Datos I");
            a1.setSigla("INF-230");
            a1.setIdEstado(1L); 

            Asignatura a2 = new Asignatura();
            a2.setNombre("Desarrollo En Fullstack");
            a2.setSigla("INF-420");
            a2.setIdEstado(1L);

            Asignatura a3 = new Asignatura();
            a3.setNombre("Autotronica");
            a3.setSigla("MCA-510");
            a3.setIdEstado(1L);

            Asignatura a4 = new Asignatura();
            a4.setNombre("Seguridad ocupacional");
            a4.setSigla("MCA-310");
            a4.setIdEstado(1L);

            asignaturaRepository.saveAll(List.of(a1, a2, a3, a4));
            log.info("[DataInitializer]: Datos iniciales de asignatura cargados con exito");
        } else {
            log.info("[DataInitializer]: La tabla Asignatura ya contiene datos. Omitiendo carga...");
        }

    }

}
