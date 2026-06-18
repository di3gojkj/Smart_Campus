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

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
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

    /* 3. TEST PARA findBySigla() */
    @Test
    @DisplayName("findBySigla() debe retornar la carrera cuando la sigla existe")
    void findBySigla_debeRetornarCarrera_cuandoExiste() {
        Optional<Carrera> resultado = carreraRepository.findBySigla("INF-001");
        assertTrue(resultado.isPresent());
        assertEquals("Ingenieria en Informatica", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findBySigla() debe retornar Optional vacio cuando la sigla no existe")
    void findBySigla_debeRetornarVacio_cuandoNoExiste() {
        Optional<Carrera> resultado = carreraRepository.findBySigla("XXX-999");
        assertFalse(resultado.isPresent());
    }

    /* 4. TEST PARA buscarPorNombreOSigla() */
    @Test
    @DisplayName("buscarPorNombreOSigla() debe encontrar carreras por nombre parcial")
    void buscarPorNombreOSigla_debeEncontrarPorNombre() {
        List<Carrera> resultado = carreraRepository.buscarPorNombreOSigla("informatica");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ingenieria en Informatica", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("buscarPorNombreOSigla() debe encontrar carreras por sigla parcial")
    void buscarPorNombreOSigla_debeEncontrarPorSigla() {
        List<Carrera> resultado = carreraRepository.buscarPorNombreOSigla("MCN");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mecanica Automotriz", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("buscarPorNombreOSigla() debe retornar lista vacia cuando no hay coincidencias")
    void buscarPorNombreOSigla_debeRetornarVacio_cuandoNoHayCoincidencias() {
        List<Carrera> resultado = carreraRepository.buscarPorNombreOSigla("XXXXX");
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    /* 5. TEST PARA findBySiglaExcludingCurrent() */
    @Test
    @DisplayName("findBySiglaExcludingCurrent() debe retornar carrera con misma sigla de otro ID")
    void findBySiglaExcludingCurrent_debeRetornarCarrera_cuandoOtraCarreraTieneLaSigla() {
        Optional<Carrera> resultado = carreraRepository
            .findBySiglaExcludingCurrent("INF-001", carrera2.getIdCarrera());
        assertTrue(resultado.isPresent());
        assertEquals("Ingenieria en Informatica", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findBySiglaExcludingCurrent() debe retornar vacio cuando la sigla pertenece al mismo ID")
    void findBySiglaExcludingCurrent_debeRetornarVacio_cuandoLaSiglaEsDeLaMismaCarrera() {
        Optional<Carrera> resultado = carreraRepository
            .findBySiglaExcludingCurrent("INF-001", carrera1.getIdCarrera());
        assertFalse(resultado.isPresent());
    }
}
