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

import com.SCampus.curso_seccion.model.Curso;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de cursos en memoria H2")
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Curso cursoEjemplo;

    @BeforeEach
    void setUp() {
        // Insertamos un curso base con formato DD/MM/AA de 8 caracteres
        cursoEjemplo = entityManager.persistAndFlush(
            new Curso(null, "16/06/26")
        );
    }

    @Test
    @DisplayName("findAll() debe retornar la lista de todos los cursos insertados")
    void findAll_debeRetornarTodosLosCursos() {
        List<Curso> cursos = cursoRepository.findAll();

        assertNotNull(cursos);
        assertEquals(1, cursos.size());
    }

    @Test
    @DisplayName("findByFechaCreacion() debe retornar un Optional con el curso si la fecha coincide")
    void findByFechaCreacion_debeRetornarCurso_cuandoExiste() {
        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("16/06/26");

        assertTrue(resultado.isPresent());
        assertEquals("16/06/26", resultado.get().getFechaCreacion());
    }

    @Test
    @DisplayName("findByFechaCreacion() debe retornar un Optional vacío si la fecha no existe")
    void findByFechaCreacion_debeRetornarVacio_cuandoNoExiste() {
        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("01/01/00");

        assertFalse(resultado.isPresent());
    }
}
