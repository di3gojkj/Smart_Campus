package com.cur_eva.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import feign.FeignException;

import com.cur_eva.client.EvaluacionClient;
import com.cur_eva.client.CursoClient; // 🛠️ INYECTADO NUEVO CLIENTE FEIGN
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.dto.EvaluacionResponseDTO;
import com.cur_eva.dto.TipoEvaluacionResponseDTO;
import com.cur_eva.dto.CursoResponseDTO; // 🛠️ INYECTADO DTO ESPEJO LOCAL
import com.cur_eva.model.CursoEvaluacion;
import com.cur_eva.repository.CursoEvaluacionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoEvaluacionService (Enlazado Distribuido)")
public class CursoEvaluacionServiceTest {

    @Mock
    private CursoEvaluacionRepository cursoEvaluacionRepository;

    @Mock
    private EvaluacionClient evaluacionClient;

    @Mock
    private CursoClient cursoClient; // 🛠️ MOCK DEL NUEVO CLIENTE DE INTEGRACIÓN

    @InjectMocks
    private CursoEvaluacionService cursoEvaluacionService;

    private CursoEvaluacion entidadMock;
    private CursoEvaluacionRequestDTO requestDTOMock;
    private EvaluacionResponseDTO evaluacionMock;
    private TipoEvaluacionResponseDTO tipoMock;
    private CursoResponseDTO cursoResponseMock; // 🛠️ DTO MOCK LOCAL

    @BeforeEach
    void setUp() {
        // 1. Inicialización de la entidad local institucional
        entidadMock = new CursoEvaluacion();
        entidadMock.setIdCursoEvaluacion(1L);
        entidadMock.setNombre("ACTIVO");
        entidadMock.setIdCurso(12L);
        entidadMock.setIdEvaluacion(100L);
        entidadMock.setFCreacion("2026-06-21");
        entidadMock.setFApertura("2026-06-20");
        entidadMock.setFCierre("2026-07-20");

        // 2. Inicialización del DTO de entrada para persistencia
        requestDTOMock = new CursoEvaluacionRequestDTO();
        requestDTOMock.setNombre("ACTIVO");
        requestDTOMock.setIdCurso(12L);
        requestDTOMock.setIdEvaluacion(100L);
        requestDTOMock.setFApertura("2026-06-20");
        requestDTOMock.setFCierre("2026-07-20");

        // 3. Mockeo puro con Mockito para evitar errores de Classpath o Lombok
        evaluacionMock = mock(EvaluacionResponseDTO.class);
        when(evaluacionMock.getId_Evaluacion()).thenReturn(100L);
        when(evaluacionMock.getNombre()).thenReturn("Certamen 1");
        when(evaluacionMock.getPorcentaje()).thenReturn(30.0);
        when(evaluacionMock.getIdTipoEval()).thenReturn(2L);

        tipoMock = mock(TipoEvaluacionResponseDTO.class);
        when(tipoMock.getIdTipoEval()).thenReturn(2L);
        when(tipoMock.getNombreTipo()).thenReturn("Certamen");

        // 🛠️ 4. Mockeo de la respuesta procedente de curso_seccion (Puerto 8080)
        cursoResponseMock = mock(CursoResponseDTO.class);
        when(cursoResponseMock.getId()).thenReturn(12L);
        when(cursoResponseMock.getNombre()).thenReturn("Programación Orientada a Objetos");
        when(cursoResponseMock.getFechaCreacion()).thenReturn("20/06/26");
    }

    @Test
    @DisplayName("obtenerTodos() - Debe retornar DTOs con metadatos externos de evaluaciones y cursos enriquecidos")
    void obtenerTodos_DebeRetornarListaEnriquecida_CuandoTodosLosMicroserviciosResponden() {
        // Arrange
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(entidadMock));
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(evaluacionClient.buscarTipoPorId(2L)).thenReturn(tipoMock);
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock); // Simula curso_seccion

        // Act
        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());
        assertEquals("Certamen 1", resultado.get(0).getNombreEvaluacion());
        assertEquals("Programación Orientada a Objetos", resultado.get(0).getNombreCurso());
        assertEquals("20/06/26", resultado.get(0).getFechaCreacionCurso());

        verify(cursoEvaluacionRepository, times(1)).findAll();
        verify(evaluacionClient, times(1)).buscarPorId(100L);
        verify(cursoClient, times(1)).buscarCursoPorId(12L);
    }

    @Test
    @DisplayName("obtenerTodos() - Debe ser tolerante y resiliente si el microservicio de cursos falla")
    void obtenerTodos_DebeRetornarDatosLocales_CuandoFallaCursoSeccion() {
        // Arrange
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(entidadMock));
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(evaluacionClient.buscarTipoPorId(2L)).thenReturn(tipoMock);
        when(cursoClient.buscarCursoPorId(12L)).thenThrow(new RuntimeException("Error en curso_seccion (Timeout)"));

        // Act
        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals("Certamen 1", resultado.get(0).getNombreEvaluacion());
        assertEquals("Curso no disponible", resultado.get(0).getNombreCurso()); // Capturado por el bloque catch de resiliencia
        
        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardar() - Debe registrar localmente si pasa la validación perimetral de curso y evaluación")
    void guardar_DebeAlmacenar_CuandoCursoYEvaluacionExistenEnRemoto() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock); // Validación distribuida 1
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock); // Validación distribuida 2
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenReturn(entidadMock);

        // Act
        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.guardar(requestDTOMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        verify(cursoEvaluacionRepository, never()); // Verifica aislamiento
        verify(cursoClient, times(2)).buscarCursoPorId(12L); // Una para validar en guardar, otra al mapear en toResponseDTO
        verify(cursoEvaluacionRepository, times(1)).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción si Feign reporta un 404 del curso en curso_seccion")
    void guardar_DebeLanzarExcepcion_CuandoCursoNoExisteEnCursoSeccion() {
        // Arrange
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        
        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(cursoClient.buscarCursoPorId(12L)).thenThrow(feignNotFound); // Bloqueo perimetral en la primera validación

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        verify(evaluacionClient, never()).buscarPorId(any(Long.class)); // Bloquea la ejecución antes de llamar al segundo MS
        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }
}



