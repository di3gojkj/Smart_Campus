package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CursoService")
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso cursoEjemplo;
    private CursoRequestDTO requestDtoPrueba;

    @BeforeEach
    void setUp() {
        cursoEjemplo = new Curso(12L, "14/06/26");
        requestDtoPrueba = new CursoRequestDTO("14/06/26");
    }

    @Test
    @DisplayName("obtenerTodos() retorna la lista de DTO de todos los cursos registrados")
    void obtenerTodos_debeRetornarListaDeCursos() {
        when(cursoRepository.findAll()).thenReturn(List.of(cursoEjemplo));

        List<CursoResponseDTO> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("14/06/26", resultado.get(0).getFechaCreacion());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar lista vacía si no existen cursos en MySQL")
    void obtenerTodos_debeRetornarListaVacia_SiNoHayCursos() {
        when(cursoRepository.findAll()).thenReturn(List.of());

        List<CursoResponseDTO> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardarCurso() debe registrar exitosamente el curso si la fecha es única")
    void guardarCurso_debeAlmacenar_cuandoFechaEsUnica() {
        when(cursoRepository.findByFechaCreacion("14/06/26")).thenReturn(Optional.empty());
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoEjemplo);

        CursoResponseDTO resultado = cursoService.guardarCurso(requestDtoPrueba);

        assertNotNull(resultado);
        assertEquals(12L, resultado.getId());
        assertEquals("14/06/26", resultado.getFechaCreacion());
        verify(cursoRepository, times(1)).findByFechaCreacion("14/06/26");
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("guardarCurso() debe lanzar RuntimeException si la fecha del curso ya existe")
    void guardarCurso_debeLanzarExcepcion_cuandoFechaDuplicada() {
        when(cursoRepository.findByFechaCreacion("14/06/26")).thenReturn(Optional.of(cursoEjemplo));

        assertThrows(RuntimeException.class, () -> {
            cursoService.guardarCurso(requestDtoPrueba);
        });

        verify(cursoRepository, times(1)).findByFechaCreacion("14/06/26");
        verify(cursoRepository, never()).save(any(Curso.class));
    }
}
