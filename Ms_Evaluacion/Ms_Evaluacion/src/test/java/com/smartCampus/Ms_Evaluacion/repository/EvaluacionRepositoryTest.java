package com.smartCampus.Ms_Evaluacion.repository;

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

import com.smartCampus.Ms_Evaluacion.model.Evaluacion;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de Integracion: EvaluacionRepository")
public class EvaluacionRepositoryTest {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TipoEvaluacion tipoCertamen;
    private Evaluacion eval1;
    private Evaluacion eval2;

    @BeforeEach
    void setUp() {
        tipoCertamen = entityManager.persistAndFlush(
            new TipoEvaluacion(null, "Certamen", null));

        eval1 = entityManager.persistAndFlush(
            new Evaluacion(null, "Certamen 1", 25.0, tipoCertamen));

        eval2 = entityManager.persistAndFlush(
            new Evaluacion(null, "Certamen 2", 30.0, tipoCertamen));
    }

    /* 1. TEST PARA findAll() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findAll() debe retornar todas las evaluaciones de la BD")
    void findAll_debeRetornarTodasLasEvaluaciones() {
        List<Evaluacion> evaluaciones = evaluacionRepository.findAll();
        assertNotNull(evaluaciones);
        assertEquals(2, evaluaciones.size());
    }

    /* 2. TEST PARA findById() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findById() debe retornar Optional con la evaluación cuando existe")
    void findById_debeRetornarEvaluacion_cuandoExiste() {
        Optional<Evaluacion> resultado = evaluacionRepository.findById(eval1.getIdEvaluacion());
        assertTrue(resultado.isPresent());
        assertEquals("Certamen 1", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacío cuando no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Evaluacion> resultado = evaluacionRepository.findById(9999L);
        assertFalse(resultado.isPresent());
    }

    /* 3. TEST PARA existsByNameAndTipoExcludingId() */
    @Test
    @DisplayName("existsByNameAndTipoExcludingId() debe detectar conflicto de nombre en edición")
    void existsByNameAndTipoExcludingId_debeRetornarTrue_cuandoHayConflicto() {
        boolean conflicto = evaluacionRepository.existsByNameAndTipoExcludingId(
                "Certamen 1", 
                tipoCertamen.getIdTipoEval(), 
                eval2.getIdEvaluacion()
        );
        assertTrue(conflicto);
    }

    /* 4. TEST PARA findByTipo() */
    @Test
    @DisplayName("findByTipo() debe retornar todas las evaluaciones amarradas a ese tipo")
    void findByTipo_debeRetornarListaEvaluaciones() {
        List<Evaluacion> resultado = evaluacionRepository.findByTipo(tipoCertamen.getIdTipoEval());
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

}
