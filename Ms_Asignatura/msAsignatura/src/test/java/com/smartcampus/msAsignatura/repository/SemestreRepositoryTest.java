package com.smartcampus.msAsignatura.repository;

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

import com.smartcampus.msAsignatura.model.Semestre;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de Integración: SemestreRepository")
public class SemestreRepositoryTest {

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Semestre sem1;
    private Semestre sem2;

    @BeforeEach
    void setUp() {
        sem1 = entityManager.persistAndFlush(
            new Semestre(null, "2026-1", 1L)
        );

        sem2 = entityManager.persistAndFlush(
            new Semestre(null, "2026-2", 1L)
        );   
    }

    /* 1. TEST PARA findAll() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findAll() debe retornar todos los semestres de la BD")
    void findAll_debeRetornarTodosLosSemestres() {
        List<Semestre> semestres = semestreRepository.findAll();
        assertNotNull(semestres);
        assertEquals(2, semestres.size());
    }

    /* 2. TEST PARA findById() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findById() debe retornar Optional con el semestre cuando existe")
    void findById_debeRetornarSemestre_cuandoExiste() {
        Optional<Semestre> resultado = semestreRepository.findById(sem1.getIdSemestre());
        assertTrue(resultado.isPresent());
        assertEquals("2026-1", resultado.get().getNombre());
    }
    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Semestre> resultado = semestreRepository.findById(9999L);
        assertFalse(resultado.isPresent());
    }

    /* 3. TEST PARA findByNombreIgnoreCase() */
    @Test
    @DisplayName("findByNombreIgnoreCase debe encontrar el semestre ignorando mayusculas")
    void findByNombreIgnoreCase_debeEncontrarSemestre() {
        Optional<Semestre> resultado = semestreRepository.findByNombreIgnoreCase("2026-1");
        
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdEstado());
    }

    /* 4. TEST PARA @Query: listarSemestreCronologicos() */
    @Test
    @DisplayName("listarSemestreCronologicos debe devolver la lista ordenada ascendentemente")
    void listarSemestreCronologicos_debeOrdenarCorrectamente() {
        List<Semestre> ordenados = semestreRepository.listarSemestreCronologicos();
        
        assertNotNull(ordenados);
        assertEquals(2, ordenados.size());
        
        assertEquals("2026-1", ordenados.get(0).getNombre());
    }

}
