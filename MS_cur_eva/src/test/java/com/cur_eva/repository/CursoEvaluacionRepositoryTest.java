package com.cur_eva.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cur_eva.model.CursoEvaluacion;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoEvaluacionRepository")
public class CursoEvaluacionRepositoryTest {

    @Mock
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    private CursoEvaluacion estadoBase;

    @BeforeEach
    void setUp() {
        estadoBase = new CursoEvaluacion();
        estadoBase.setIdCursoEvaluacion(1L);
        estadoBase.setNombre("ACTIVO");
        estadoBase.setIdCurso(12L);
        estadoBase.setIdEvaluacion(1L);
        estadoBase.setFCreacion("2026-06-21");
        estadoBase.setFApertura("2026-06-20");
        estadoBase.setFCierre("2026-07-20");
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe localizar el registro omitiendo mayúsculas y minúsculas")
    void findByNombreIgnoreCase_DebeRetornarEntidad_CuandoExiste() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("activo")).thenReturn(Optional.of(estadoBase));

        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("activo");

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
        assertEquals(12L, resultado.get().getIdCurso());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe retornar Optional vacío si el nombre no se encuentra en el sistema")
    void findByNombreIgnoreCase_DebeRetornarVacio_CuandoNoExiste() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("INEXISTENTE")).thenReturn(Optional.empty());

        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.findByNombreIgnoreCase("INEXISTENTE");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorNombreExacto() - Debe ejecutar la consulta JPQL personalizada de forma exitosa")
    void buscarPorNombreExacto_DebeRetornarEntidad_CuandoFiltroCoincide() {
        when(cursoEvaluacionRepository.buscarPorNombreExacto("ACTIVO")).thenReturn(Optional.of(estadoBase));

        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.buscarPorNombreExacto("ACTIVO");

        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
        assertNotNull(resultado.get().getIdCursoEvaluacion());
    }

    @Test
    @DisplayName("buscarPorNombreExacto() - Debe retornar Optional vacío si la query JPQL no encuentra coincidencias")
    void buscarPorNombreExacto_DebeRetornarVacio_CuandoFiltroNoCoincide() {
        when(cursoEvaluacionRepository.buscarPorNombreExacto("DESACTIVADO")).thenReturn(Optional.empty());

        Optional<CursoEvaluacion> resultado = cursoEvaluacionRepository.buscarPorNombreExacto("DESACTIVADO");
        assertFalse(resultado.isPresent());
    }
}
