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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cur_eva.client.EvaluacionClient; 
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.dto.EvaluacionResponseDTO; 
import com.cur_eva.exception.CursoEvaluacionNotFoundException;
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

import feign.FeignException; // INYECTADO: Control de excepciones de red de Feign

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de CursoEvaluacionService")
public class CursoEvaluacionServiceTest {

    @Mock
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    @Mock
    private EvaluacionClient evaluacionClient; // INYECTADO: Mock requerido por la nueva validación remota

    @InjectMocks
    private CursoEvaluacionService cursoEvaluacionService;

    private CursoEvaluacion evaluacionEjemplo;
    private CursoEvaluacionRequestDTO requestDtoPrueba;

    @BeforeEach
    void setUp() {
       
        evaluacionEjemplo = new CursoEvaluacion(1L, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20");
        
        requestDtoPrueba = new CursoEvaluacionRequestDTO("ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20", 5L);
    }

    @Test
    @DisplayName("obtenerTodos() retorna la lista de DTO de todas las evaluaciones")
    void obtenerTodos_debeRetornarListaDeEvaluaciones() {
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(evaluacionEjemplo));

        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());
        // CORREGIDO: Mapeo correcto al getter de Fecha Apertura según tu capa service real
        assertEquals("2026-06-15", resultado.get(0).getFApertura());

        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos() debe retornar una lista vacía cuando no hay registros")
    void obtenerTodos_debeRetornarListaVacia_SiNoHayRegistros() {
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of());

        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId() debe retornar el DTO cuando el ID existe")
    void obtenerPorId_debeRetornarDTO_CuandoIdExiste() {
        when(cursoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacionEjemplo));

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        assertEquals("ACTIVO", resultado.getNombre());
        verify(cursoEvaluacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() debe lanzar CursoEvaluacionNotFoundException cuando el ID no existe")
    void obtenerPorId_debeLanzarExcepcion_CuandoIdNoExiste() {
        when(cursoEvaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CursoEvaluacionNotFoundException.class, () -> {
            cursoEvaluacionService.obtenerPorId(99L);
        });

        verify(cursoEvaluacionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardar() debe almacenar con éxito si el nombre no está duplicado y la evaluación remota existe")
    void guardar_debeAlmacenar_CuandoNombreEsUnico() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        
        // Simulamos respuesta HTTP exitosa desde Ms_Evaluacion
        EvaluacionResponseDTO evalMock = new EvaluacionResponseDTO(5L, "Examen Base", 15.0, 1L, "Parcial");
        when(evaluacionClient.buscarPorId(5L)).thenReturn(evalMock);
        
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenReturn(evaluacionEjemplo);

        // Act
        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.guardar(requestDtoPrueba);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        assertEquals("ACTIVO", resultado.getNombre());

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        verify(evaluacionClient, times(1)).buscarPorId(5L);
        verify(cursoEvaluacionRepository, times(1)).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("guardar() debe lanzar RuntimeException cuando el nombre ya se encuentra registrado localmente")
    void guardar_debeLanzarExcepcion_CuandoNombreEstaDuplicado() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.of(evaluacionEjemplo));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDtoPrueba);
        });

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        verify(evaluacionClient, never()).buscarPorId(any(Long.class));
        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("guardar() debe lanzar RuntimeException si el cliente Feign devuelve 404 (La evaluación remota no existe)")
    void guardar_debeLanzarExcepcion_CuandoEvaluacionRemotaNoExiste() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        
        // Simulamos que Feign arroja un error 404 Not Found al consumir Ms_Evaluacion
        FeignException.NotFound feignException = org.mockito.Mockito.mock(FeignException.NotFound.class);
        doThrow(feignException).when(evaluacionClient).buscarPorId(5L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDtoPrueba);
        });

        assertTrue(exception.getMessage().contains("no existe en el sistema remoto"));
        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        verify(evaluacionClient, times(1)).buscarPorId(5L);
        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }
}

