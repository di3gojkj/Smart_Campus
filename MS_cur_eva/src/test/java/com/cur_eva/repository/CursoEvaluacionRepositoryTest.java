package com.cur_eva.repository;

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

import com.cur_eva.model.CursoEvaluacion;


@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de evaluaciones en memoria H2")
public class CursoEvaluacionRepositoryTest {

    @Autowired
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    @Autowired
    private TestEntityManager entityManager;

  
    private CursoEvaluacion evaluacionActiva;
    private CursoEvaluacion evaluacionCerrada;

    @BeforeEach
    void setUp() {
        
        evaluacionActiva = entityManager.persistAndFlush(
            new CursoEvaluacion(null, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20")
        );
        evaluacionCerrada = entityManager.persistAndFlush(
            new CursoEvaluacion(null, "CERRADO", "2026-05-10", "2026-06-10", "2026-05-15")
        );
    }

    @Test
    @DisplayName("findAll() debe retornar todas las evaluaciones registradas en la base de datos")
    void findAll_debeRetornarTodasLasEvaluaciones() {
        
        List<CursoEvaluacion> lista = cursoEvaluacionRepository.findAll();

        
        assertNotNull(lista);
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe localizar la evaluación ignorando mayúsculas y minúsculas")
    void findByNombreIgnoreCase_debeRetornarEvaluacion_cuandoExiste() {
        
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("activo");

        
        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
        assertEquals("2026-06-15", resultado.get().getFCreacion());
    }

    @Test
    @DisplayName("buscarPorNombreExacto() debe ejecutar la consulta JPQL personalizada de manera correcta")
    void buscarPorNombreExacto_debeRetornarEvaluacion() {
        
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.buscarPorNombreExacto("CERRADO");

        
        assertTrue(resultado.isPresent());
        assertEquals("CERRADO", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe retornar un Optional vacío si el nombre no existe en los registros")
    void findByNombreIgnoreCase_debeRetornarVacio_cuandoNoExiste() {
        
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("INEXISTENTE");

        assertFalse(resultado.isPresent());
    }
}
