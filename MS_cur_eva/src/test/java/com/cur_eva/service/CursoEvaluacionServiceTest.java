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
import com.cur_eva.client.CursoClient;
import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.dto.EvaluacionResponseDTO;
import com.cur_eva.dto.TipoEvaluacionResponseDTO;
import com.cur_eva.dto.CursoResponseDTO;
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
    private CursoClient cursoClient;

    @InjectMocks
    private CursoEvaluacionService cursoEvaluacionService;

    private CursoEvaluacion entidadMock;
    private CursoEvaluacionRequestDTO requestDTOMock;
    private EvaluacionResponseDTO evaluacionMock;
    private TipoEvaluacionResponseDTO tipoMock;
    private CursoResponseDTO cursoResponseMock;

    @BeforeEach
    void setUp() {
        entidadMock = new CursoEvaluacion();
        entidadMock.setIdCursoEvaluacion(1L);
        entidadMock.setNombre("ACTIVO");
        entidadMock.setIdCurso(12L);
        entidadMock.setIdEvaluacion(100L);
        entidadMock.setFCreacion("2026-06-21");
        entidadMock.setFApertura("2026-06-20");
        entidadMock.setFCierre("2026-07-20");

        requestDTOMock = new CursoEvaluacionRequestDTO();
        requestDTOMock.setNombre("ACTIVO");
        requestDTOMock.setIdCurso(12L);
        requestDTOMock.setIdEvaluacion(100L);
        requestDTOMock.setFApertura("2026-06-20");
        requestDTOMock.setFCierre("2026-07-20");

        evaluacionMock = new EvaluacionResponseDTO();
        evaluacionMock.setId_Evaluacion(100L);
        evaluacionMock.setNombre("Certamen 1");
        evaluacionMock.setPorcentaje(30.0);
        evaluacionMock.setIdTipoEval(2L);

        tipoMock = new TipoEvaluacionResponseDTO();
        tipoMock.setIdTipoEval(2L);
        tipoMock.setNombreTipo("Certamen");

        cursoResponseMock = new CursoResponseDTO();
        cursoResponseMock.setId(12L);
        cursoResponseMock.setNombre("Programación Orientada a Objetos");
        cursoResponseMock.setFechaCreacion("20/06/26");
    }

    @Test
    @DisplayName("obtenerTodos() - Debe retornar DTOs con metadatos externos de evaluaciones y cursos enriquecidos")
    void obtenerTodos_DebeRetornarListaEnriquecida_CuandoTodosLosMicroserviciosResponden() {
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(entidadMock));
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(evaluacionClient.buscarTipoPorId(2L)).thenReturn(tipoMock);
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);

        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

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
        when(cursoEvaluacionRepository.findAll()).thenReturn(List.of(entidadMock));
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(evaluacionClient.buscarTipoPorId(2L)).thenReturn(tipoMock);
        when(cursoClient.buscarCursoPorId(12L)).thenThrow(new RuntimeException("Error en curso_seccion (Timeout)"));

        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals("Certamen 1", resultado.get(0).getNombreEvaluacion());
        assertEquals("Curso no disponible", resultado.get(0).getNombreCurso());
        
        verify(cursoEvaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardar() - Debe registrar localmente si pasa la validación perimetral de curso y evaluación")
    void guardar_DebeAlmacenar_CuandoCursoYEvaluacionExistenEnRemoto() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenReturn(entidadMock);

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.guardar(requestDTOMock);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        verify(cursoEvaluacionRepository, never()).findAll();
        verify(cursoClient, times(2)).buscarCursoPorId(12L);
        verify(cursoEvaluacionRepository, times(1)).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción si Feign reporta un 404 del curso en curso_seccion")
    void guardar_DebeLanzarExcepcion_CuandoCursoNoExisteEnCursoSeccion() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase("ACTIVO")).thenReturn(Optional.empty());
        
        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(cursoClient.buscarCursoPorId(12L)).thenThrow(feignNotFound);

        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        verify(cursoEvaluacionRepository, times(1)).findByNombreIgnoreCase("ACTIVO");
        verify(evaluacionClient, never()).buscarPorId(any(Long.class));
        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }
}
