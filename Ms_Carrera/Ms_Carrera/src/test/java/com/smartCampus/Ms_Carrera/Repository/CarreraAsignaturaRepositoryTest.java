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
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Test de integracion: Relacion carreraAsignatura")
public class CarreraAsignaturaRepositoryTest {

    @Autowired
    private CarreraAsignaturaRepository carreraAsignaturaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Carrera carrera1;

    private CarreraAsignatura rel1;
    private CarreraAsignatura rel2;
    
    @BeforeEach
    void setUp(){

        carrera1 = entityManager.persistAndFlush(
            new Carrera(null, "Ingenieria en Informatica", "INF-001", 1L));

        rel1 = entityManager.persistAndFlush(
            new CarreraAsignatura(null, carrera1, 3L, 1l)
        );
        rel2 = entityManager.persistAndFlush(
            new CarreraAsignatura(null, carrera1, 1L, 1l)
        );
    }

    /*TEST PARA findAll() -- Heredado de JPArepository */
    @Test
    @DisplayName("findAll() debe retornar todos los registros de relación insertados")

    void findAll_debeRetornarTodasLasCarrerasAsignaturas() {
        List<CarreraAsignatura> CarreraAsignaturas = carreraAsignaturaRepository.findAll();

        assertNotNull(CarreraAsignaturas);
        assertEquals(2, CarreraAsignaturas.size());
    }

    /*TEST PARA findById() -- Heredado de JPArepository */
    @Test
    @DisplayName("findById() debe retornar relación cuando el ID existe")
    void findById_debeRetornarProducto_cuandoExiste(){
        Optional<CarreraAsignatura> resultado = carreraAsignaturaRepository.findById(rel1.getIdCarreraAsignatura());

        assertTrue(resultado.isPresent());
        assertEquals(3L, resultado.get().getIdAsignatura());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste(){
        Optional<CarreraAsignatura> resultado = carreraAsignaturaRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    
}
