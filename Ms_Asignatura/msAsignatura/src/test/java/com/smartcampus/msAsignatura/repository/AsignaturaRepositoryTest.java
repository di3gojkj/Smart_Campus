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

import com.smartcampus.msAsignatura.model.Asignatura;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de Integracion: AsignaturaRepository")
public class AsignaturaRepositoryTest {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Asignatura asig1;
    private Asignatura asig2;

    @BeforeEach
    void setUp() {
        asig1 = entityManager.persistAndFlush(
            new Asignatura(null, "Desarrollo en Fullstack", "INF-230", 1L));
        asig2 = entityManager.persistAndFlush(
            new Asignatura(null, "Bases de Datos I", "INF-420", 1L));
    }

    /* 1. TEST PARA findAll() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findAll() debe retornar todas las asignaturas de la BD")
    void findAll_debeRetornarTodasLasAsignaturas() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();
        assertNotNull(asignaturas);
        assertEquals(2, asignaturas.size());
    }

    /* 2. TEST PARA findById() -- Heredado de JpaRepository */
    @Test
    @DisplayName("findById() debe retornar Optional con la asignatura cuando existe")
    void findById_debeRetornarAsignatura_cuandoExiste() {
        Optional<Asignatura> resultado = asignaturaRepository.findById(asig1.getIdAsignatura());
        assertTrue(resultado.isPresent());
        assertEquals("Desarrollo en Fullstack", resultado.get().getNombre());
    }
    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Asignatura> resultado = asignaturaRepository.findById(9999L);
        assertFalse(resultado.isPresent());
    }

    /* 3. TEST PARA findByNombreContainingIgnoreCase() */
    @Test
    @DisplayName("findByNombreContainingIgnoreCase encuentra asignaturas por coincidencia de texto")
    void findByNombreContainingIgnoreCase_debeEncontrarAsignaturas() {
        List<Asignatura> resultado = asignaturaRepository.findByNombreContainingIgnoreCase("datos");
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bases de Datos I", resultado.get(0).getNombre());
    }

    /* 4. TEST PARA findBySiglaIgnoreCase() */
    @Test
    @DisplayName("findBySiglaIgnoreCase encuentra por sigla exacta ignorando mayusculas")
    void findBySiglaIgnoreCase_debeRetornarAsignatura() {
        Optional<Asignatura> resultado = asignaturaRepository.findBySiglaIgnoreCase("inf-230");
        
        assertTrue(resultado.isPresent());
        assertEquals("Desarrollo en Fullstack", resultado.get().getNombre());
    }

    /* 5. TEST PARA buscarPorEstadoOrdenadoAlfabeticamente() */
    @Test
    @DisplayName("buscarPorEstadoOrdenadoAlfabeticamente retorna solo activos ordenados A-Z")
    void buscarPorEstadoOrdenadoAlfabeticamente_debeFiltrarYOrdenar() {
        List<Asignatura> activos = asignaturaRepository.buscarPorEstadoOrdenadoAlfabeticamente(1L);
        
        assertNotNull(activos);
        assertEquals(2, activos.size());
        assertEquals("Bases de Datos I", activos.get(0).getNombre());
    }

}
