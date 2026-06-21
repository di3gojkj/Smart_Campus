package com.cur_eva.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import com.cur_eva.model.CursoEvaluacion;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("Pruebas Unitarias desde cero para CursoEvaluacionRepository")
public class CursoEvaluacionRepositoryTest {

    @Autowired
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    @Autowired
    private EntityManager entityManager;

    private CursoEvaluacion estadoBase;

    @BeforeEach
    void setUp() {
        // Inicializamos un registro de prueba respetando los campos obligatorios de tu entidad
        estadoBase = new CursoEvaluacion();
        estadoBase.setNombre("ACTIVO");
        estadoBase.setIdCurso(12L);
        estadoBase.setIdEvaluacion(1L);
        estadoBase.setFCreacion("2026-06-21");
        estadoBase.setFApertura("2026-06-20");
        estadoBase.setFCierre("2026-07-20");

        entityManager.persist(estadoBase);
        entityManager.flush();
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe localizar el registro omitiendo mayúsculas y minúsculas")
    void findByNombreIgnoreCase_DebeRetornarEntidad_CuandoExiste() {
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("activo");

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
        assertEquals(12L, resultado.get().getIdCurso());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe retornar Optional vacío si el nombre no se encuentra en el sistema")
    void findByNombreIgnoreCase_DebeRetornarVacio_CuandoNoExiste() {
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("INEXISTENTE");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorNombreExacto() - Debe ejecutar la consulta JPQL personalizada de forma exitosa")
    void buscarPorNombreExacto_DebeRetornarEntidad_CuandoFiltroCoincide() {
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.buscarPorNombreExacto("ACTIVO");

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
        assertNotNull(resultado.get().getIdCursoEvaluacion());
    }

    @Test
    @DisplayName("buscarPorNombreExacto() - Debe retornar Optional vacío si la query JPQL no encuentra coincidencias")
    void buscarPorNombreExacto_DebeRetornarVacio_CuandoFiltroNoCoincide() {
        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.buscarPorNombreExacto("DESACTIVADO");
        assertFalse(resultado.isPresent());
    }
}
