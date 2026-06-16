package com.SCampus.curso_seccion.repository;

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
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest; // Sincronizado con tu pom.xml institucional
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.SCampus.curso_seccion.model.Seccion;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de secciones en memoria H2")
public class SeccionRepositoryTest {

    @Autowired
    private SeccionRepository seccionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Seccion seccionA;
    private Seccion seccionB;

    @BeforeEach
    void setUp() {
        // Insertamos secciones vinculadas de forma lógica al curso ID 12
        seccionA = entityManager.persistAndFlush(new Seccion(null, "Sección A", 12L));
        seccionB = entityManager.persistAndFlush(new Seccion(null, "Sección B", 12L));
    }

    @Test
    @DisplayName("findByIdCurso() debe retornar el listado completo de secciones asociadas a ese curso padre")
    void findByIdCurso_debeRetornarListaDeSecciones() {
        List<Seccion> resultado = seccionRepository.findByIdCurso(12L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Sección A", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe localizar el registro ignorando mayúsculas o minúsculas")
    void findByNombreIgnoreCase_debeRetornarSeccion_cuandoExiste() {
        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("SECCIÓN B");

        assertTrue(resultado.isPresent());
        assertEquals("Sección B", resultado.get().getNombre());
        assertEquals(12L, resultado.get().getIdCurso());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe retornar un Optional vacío si el nombre no se encuentra")
    void findByNombreIgnoreCase_debeRetornarVacio_cuandoNoExiste() {
        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("Sección Inexistente");

        assertFalse(resultado.isPresent());
    }
}
