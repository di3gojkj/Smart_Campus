package com.SCampus.curso_seccion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import com.SCampus.curso_seccion.model.Curso;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("Pruebas Unitarias desde cero para CursoRepository")
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("findAll() - Debe retornar todos los cursos guardados")
    void findAll_DebeRetornarListaDeCursos() {
        Curso curso = new Curso(null, "14/06/26");
        entityManager.persist(curso);
        entityManager.flush();

        List<Curso> resultado = cursoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("findByFechaCreacion() - Debe retornar el curso si la fecha coincide")
    void findByFechaCreacion_DebeRetornarCurso_CuandoExiste() {
        Curso curso = new Curso(null, "25/12/26");
        entityManager.persist(curso);
        entityManager.flush();

        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("25/12/26");

        assertTrue(resultado.isPresent());
        assertEquals("25/12/26", resultado.get().getFechaCreacion());
    }

    @Test
    @DisplayName("findByFechaCreacion() - Debe retornar vacío si la fecha no existe")
    void findByFechaCreacion_DebeRetornarVacio_CuandoNoExiste() {
        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("00/00/00");
        assertTrue(resultado.isEmpty());
    }
}
