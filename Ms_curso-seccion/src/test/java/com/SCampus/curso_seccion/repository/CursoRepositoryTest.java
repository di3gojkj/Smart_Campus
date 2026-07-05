package com.SCampus.curso_seccion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.model.Curso;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoRepository")
public class CursoRepositoryTest {

    @Mock
    private CursoRepository cursoRepository;

    @Test
    @DisplayName("findAll() - Debe retornar todos los cursos guardados")
    void findAll_DebeRetornarListaDeCursos() {
        Curso curso = new Curso(1L, "14/06/26");
        when(cursoRepository.findAll()).thenReturn(List.of(curso));

        List<Curso> resultado = cursoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("findByFechaCreacion() - Debe retornar el curso si la fecha coincide")
    void findByFechaCreacion_DebeRetornarCurso_CuandoExiste() {
        Curso curso = new Curso(1L, "25/12/26");
        when(cursoRepository.findByFechaCreacion("25/12/26")).thenReturn(Optional.of(curso));

        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("25/12/26");

        assertTrue(resultado.isPresent());
        assertEquals("25/12/26", resultado.get().getFechaCreacion());
    }

    @Test
    @DisplayName("findByFechaCreacion() - Debe retornar vacío si la fecha no existe")
    void findByFechaCreacion_DebeRetornarVacio_CuandoNoExiste() {
        when(cursoRepository.findByFechaCreacion("00/00/00")).thenReturn(Optional.empty());

        Optional<Curso> resultado = cursoRepository.findByFechaCreacion("00/00/00");
        assertTrue(resultado.isEmpty());
    }
}
