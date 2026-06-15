package com.smartCampus.Ms_Carrera.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import com.smartCampus.Ms_Carrera.model.Carrera;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de Carreras en memoria")

public class CarreraRepositoryTest {

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Carrera carrera1;
    private Carrera carrera2;

    
    @BeforeEach
    void setUp() {
        // Insertamos datos base para probar
        carrera1 = entityManager.persistAndFlush(
            new Carrera(null, "Ingenieria en Informatica", "INF-001", 1L));

        carrera2 = entityManager.persistAndFlush(
            new Carrera(null,"Mecanica Automotriz","MCN-001",1L));
    }
            

    /*TEST PARA findAll() -- Heredado de JPArepository */
    @Test
    @DisplayName("FindAll() debe retornar todas las carreras insertadas")
    void findAll_debeRetornarTodasLasCarreras(){
        List<Carrera> carreras = carreraRepository.findAll();

        assertNotNull(carreras);
        assertEquals(2, carreras.size());

    }

    /*TEST PARA findById() -- Heredado de JPArepository */
    @Test
    @DisplayName("findById() debe retornar Optional con la carrera cuando existe")
    void findById_debeRetornarProducto_cuandoExiste(){
        Optional<Carrera> resultado = carreraRepository.findById(carrera1.getIdCarrera());

        assertTrue(resultado.isPresent());
        assertEquals("Ingenieria en Informatica",resultado.get().getNombre());
    }
    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<Carrera> resultado = carreraRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }
}
