package com.cur_eva.service;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.exception.CursoEvaluacionNotFoundException;
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CursoEvaluacionService")
public class CursoEvaluacionServiceTest {

    @Mock
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    @InjectMocks
    private CursoEvaluacionService cursoEvaluacionService;

    // Variables compartidas para los escenarios de prueba
    private CursoEvaluacion evaluacionEjemplo;
    private CursoEvaluacionRequestDTO requestDtoPrueba;

    @BeforeEach
    void setUp() {
        evaluacionEjemplo = new CursoEvaluacion(1L, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20");
        requestDtoPrueba = new CursoEvaluacionRequestDTO("ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20");
    }

    @Test
    @DisplayName("obtenerTodos() retorna la lista de DTO de todas las evaluaciones")
    void obtenerTodos_debeRetornarListaDeEvaluaciones() {
        // Arrange
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(evaluacionEjemplo));

        // Act
        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());
        assertEquals("2026-06-15", resultado.get(0).getFCreacion());

        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar una lista vacía cuando no hay registros")
    void obtenerTodos_debeRetornarListaVacia_SiNoHayRegistros() {
        // Arrange
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of());

        // Act
        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId() debe retornar el DTO cuando el ID existe")
    void obtenerPorId_debeRetornarDTO_CuandoIdExiste() {
        // Arrange
        when(cursoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacionEjemplo));

        // Act
        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        assertEquals("ACTIVO", resultado.getNombre());
        verify(cursoEvaluacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() debe lanzar CursoEvaluacionNotFoundException cuando el ID no existe")
    void obtenerPorId_debeLanzarExcepcion_CuandoIdNoExiste() {
        // Arrange
        when(cursoEvaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CursoEvaluacionNotFoundException.class, () -> {
            cursoEvaluacionService.obtenerPorId(99L);
        });

        verify(cursoEvaluacionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar() debe almacenar con éxito si el nombre no está duplicado")
    void guardar_debeAlmacenar_CuandoNombreEsUnico() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        // CORREGIDO: Sintaxis correcta de any(Clase.class) para Mockito
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenReturn(evaluacionEjemplo);

        // Act
        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.guardar(requestDtoPrueba);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        assertEquals("ACTIVO", resultado.getNombre());

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        // CORREGIDO: Sintaxis correcta de any(Clase.class) en el verify
        verify(cursoEvaluacionRepository, times(1)).save(any(CursoEvaluacion.class));
    }


    @Test
    @DisplayName("guardar() debe lanzar RuntimeException cuando el nombre ya se encuentra registrado")
    void guardar_debeLanzarExcepcion_CuandoNombreEstaDuplicado() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.of(evaluacionEjemplo));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDtoPrueba);
        });

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        // CORREGIDO: Sintaxis correcta de any(Clase.class) con el matcher never()
        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }
}
