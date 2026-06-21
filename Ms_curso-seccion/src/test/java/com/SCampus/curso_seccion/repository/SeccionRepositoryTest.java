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
import com.SCampus.curso_seccion.model.Seccion;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("Pruebas Unitarias desde cero para SeccionRepository")
public class SeccionRepositoryTest {

    @Autowired
    private SeccionRepository seccionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("findByIdCurso() - Debe retornar secciones asociadas al ID numérico del curso")
    void findByIdCurso_DebeRetornarListaDeSecciones() {
        Curso curso = new Curso(null, "14/06/26");
        entityManager.persist(curso);
        entityManager.flush();

        Seccion seccion = new Seccion();
        seccion.setNombre("Sección Alpha");
        seccion.setCurso(curso);
        entityManager.persist(seccion);
        entityManager.flush();

        // Se invoca findByIdCurso pasando el ID generado del curso guardado
        List<Seccion> resultado = seccionRepository.findByCurso_Id(curso.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sección Alpha", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe localizar la sección omitiendo mayúsculas")
    void findByNombreIgnoreCase_DebeRetornarSeccion_CuandoExiste() {
        Curso curso = new Curso(null, "14/06/26");
        entityManager.persist(curso);

        Seccion seccion = new Seccion();
        seccion.setNombre("Matutina");
        seccion.setCurso(curso);
        entityManager.persist(seccion);
        entityManager.flush();

        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("MATUTINA");

        assertTrue(resultado.isPresent());
        assertEquals("Matutina", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe retornar vacío si el nombre no concuerda")
    void findByNombreIgnoreCase_DebeRetornarVacio_CuandoNoExiste() {
        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("Inexistente");
        assertTrue(resultado.isEmpty());
    }
}