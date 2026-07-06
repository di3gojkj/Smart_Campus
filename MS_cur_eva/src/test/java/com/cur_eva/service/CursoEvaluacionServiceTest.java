package com.cur_eva.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("obtenerPorId() - Debe retornar el DTO consolidado si existe")
    void obtenerPorId_DebeRetornarDto_CuandoIdExiste() {
        when(cursoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(entidadMock));
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        when(evaluacionClient.buscarTipoPorId(2L)).thenReturn(tipoMock);
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdCursoEvaluacion());
        verify(cursoEvaluacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() - Debe lanzar excepción si el ID no existe localmente")
    void obtenerPorId_DebeLanzarExcepcion_CuandoIdNoExiste() {
        when(cursoEvaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción si el nombre ya existe (Duplicado)")
    void guardar_DebeLanzarExcepcion_CuandoNombreYaExiste() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase(requestDTOMock.getNombre()))
            .thenReturn(Optional.of(entidadMock));

        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción si Feign reporta un 404 de Evaluacion")
    void guardar_DebeLanzarExcepcion_CuandoEvaluacionNoExiste() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);

        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(evaluacionClient.buscarPorId(100L)).thenThrow(feignNotFound);

        assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        verify(cursoEvaluacionRepository, never()).save(any(CursoEvaluacion.class));
    }

    @Test
    @DisplayName("obtenerTodos() - Debe retornar lista vacía si la BD no tiene registros")
    void obtenerTodos_DebeRetornarListaVacia_CuandoNoHayDatos() {
        when(cursoEvaluacionRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<CursoEvaluacionResponseDTO> resultado = cursoEvaluacionService.obtenerTodos();

        assertTrue(resultado.isEmpty());
        verify(cursoEvaluacionRepository, times(1)).findAll();
        // Verifica que no llama a los Feign Clients si no hay datos que procesar
        verify(evaluacionClient, never()).buscarPorId(any());
        verify(cursoClient, never()).buscarCursoPorId(any());
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción si ocurre un error inesperado al guardar en BD")
    void guardar_DebeLanzarExcepcion_CuandoFallaBaseDeDatos() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);
        when(evaluacionClient.buscarPorId(100L)).thenReturn(evaluacionMock);
        
        // Simulamos el crasheo de la BD justo en el momento del save()
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenThrow(new RuntimeException("Error fatal interno BD"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        assertTrue(ex.getMessage().contains("Error fatal interno BD"));
    }

    @Test
    @DisplayName("toResponseDTO() - Ramas nulas: Debe manejar entidad con IDs nulos sin llamar a Feign")
    void obtenerPorId_DebeManejarIdsNulos() {
        CursoEvaluacion entidadNula = new CursoEvaluacion();
        entidadNula.setIdCursoEvaluacion(2L);
        entidadNula.setNombre("NULO");
        // Dejamos idCurso e idEvaluacion como nulos explícitamente

        when(cursoEvaluacionRepository.findById(2L)).thenReturn(Optional.of(entidadNula));

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.obtenerPorId(2L);

        assertNotNull(resultado);
        assertNull(resultado.getNombreCurso());
        assertNull(resultado.getNombreEvaluacion());
        
        verify(evaluacionClient, never()).buscarPorId(any());
        verify(cursoClient, never()).buscarCursoPorId(any());
    }

    @Test
    @DisplayName("toResponseDTO() - Catch block: Debe asignar nombres por defecto cuando falla la primera llamada a Feign")
    void obtenerPorId_AsignaValoresPorDefecto_CuandoFallaEvaluacionClient() {
        when(cursoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(entidadMock));
        
        when(evaluacionClient.buscarPorId(100L)).thenThrow(new RuntimeException("Falla remota"));

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.obtenerPorId(1L);

        assertEquals("Nombre no disponible", resultado.getNombreEvaluacion());
        assertEquals("Tipo no disponible", resultado.getNombreTipoEvaluacion());
        assertEquals("Curso no disponible", resultado.getNombreCurso());
    }

    @Test
    @DisplayName("guardar() - Ramas nulas: Debe saltar validaciones Feign si los IDs vienen nulos en el DTO")
    void guardar_DebeSaltarValidacionesRemotas_CuandoIdsNulos() {
        CursoEvaluacionRequestDTO dtoNulo = new CursoEvaluacionRequestDTO();
        dtoNulo.setNombre("NUEVO_NULO");
        
        CursoEvaluacion entidadGuardadaNula = new CursoEvaluacion();
        entidadGuardadaNula.setIdCursoEvaluacion(3L);
        entidadGuardadaNula.setNombre("NUEVO_NULO");


        when(cursoEvaluacionRepository.findByNombreIgnoreCase("NUEVO_NULO")).thenReturn(Optional.empty());
        
        when(cursoEvaluacionRepository.save(any(CursoEvaluacion.class))).thenReturn(entidadGuardadaNula);

        CursoEvaluacionResponseDTO resultado = cursoEvaluacionService.guardar(dtoNulo);

        assertNotNull(resultado);
        verify(cursoClient, never()).buscarCursoPorId(any());
        verify(evaluacionClient, never()).buscarPorId(any());
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción genérica de Feign al validar el curso (Ej. 500 Timeout)")
    void guardar_LanzaExcepcion_CuandoCursoClientLanzaFeignExceptionGenerica() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase(any())).thenReturn(Optional.empty());
        
        // Simulamos un error genérico de Feign (No un 404 NotFound)
        FeignException feignError = mock(FeignException.class);
        when(cursoClient.buscarCursoPorId(12L)).thenThrow(feignError);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        assertTrue(ex.getMessage().contains("temporalmente"));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción genérica de Feign al validar la evaluación (Ej. 500 Timeout)")
    void guardar_LanzaExcepcion_CuandoEvaluacionClientLanzaFeignExceptionGenerica() {
        when(cursoEvaluacionRepository.findByNombreIgnoreCase(any())).thenReturn(Optional.empty());
        when(cursoClient.buscarCursoPorId(12L)).thenReturn(cursoResponseMock);

        // Simulamos un error genérico de Feign
        FeignException feignError = mock(FeignException.class);
        when(evaluacionClient.buscarPorId(100L)).thenThrow(feignError);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cursoEvaluacionService.guardar(requestDTOMock);
        });

        assertTrue(ex.getMessage().contains("temporalmente"));
    }
}
