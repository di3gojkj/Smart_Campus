package com.smartCampus.Ms_Carrera.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final CarreraRepository carreraRepository;
    private final CarreraAsignaturaRepository carreraAsignaturaRepository;
    
    @Override
    public void run(String... args){
        if (carreraRepository.count() == 0) {
            log.info("[DataSeeder]: Insertando Carrera y Relaciones..");

            /*Creamos las nuevas carreras*/
            Carrera carrera1 = carreraRepository.save(new Carrera(null,"Ingenieria en Informatica","INF-001",1L));
            Carrera carrera2 = carreraRepository.save(new Carrera(null,"Mecanica Automotriz","MCA-001",1L));

            /*Al crear las nuevas carreras la usaremos para generar la relacion */

            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera1, 1L, 1L));
            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera1, 2L, 1L));
            carreraAsignaturaRepository.save(new CarreraAsignatura(null, carrera2, 3L, 1L));

            log.info("[DataSeeder]: Datos iniciales cargados correctamente");
        } else {
            log.info("[DataSeeder]: La tabla ya contiende datos, omitiendo carga inicial...");

        }
    }


}
