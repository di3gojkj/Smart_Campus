package com.smartCampus.Ms_Evaluacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.exception.EvaluacionConflictException;
import com.smartCampus.Ms_Evaluacion.exception.EvaluacionNotFoundException;
import com.smartCampus.Ms_Evaluacion.exception.TipoEvaluacionNotFoundException;
import com.smartCampus.Ms_Evaluacion.model.Evaluacion;
import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;
import com.smartCampus.Ms_Evaluacion.repository.EvaluacionRepository;
import com.smartCampus.Ms_Evaluacion.repository.TipoEvaluacionRepository;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test de integracion: EvaluacionService")
public class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private TipoEvaluacionRepository tipoEvaluacionRepository;

    @InjectMocks
    private EvaluacionService evaluacionService;

    private TipoEvaluacion tipoCertamen;
    private Evaluacion evaluacion;
    private EvaluacionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        tipoCertamen = new TipoEvaluacion(1L, "Certamen", null);
        evaluacion = new Evaluacion(1L, "Certamen 1", 30.0, tipoCertamen);
        requestDTO = new EvaluacionRequestDTO();
        requestDTO.setNombre("Certamen 1");
        requestDTO.setPorcentaje(30.0);
        requestDTO.setIdTipoEval(1L);
    }

    @Test
    @DisplayName("listarTodas() retorna la lista de DTO de todas las evaluaciones")
    void listarTodas_debeRetornarListaDeEvaluaciones() {
        when(evaluacionRepository.findAll()).thenReturn(List.of(evaluacion));

        List<EvaluacionResponseDTO> resultado = evaluacionService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Certamen 1", resultado.get(0).getNombre());
        verify(evaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodas() retorna lista vacia cuando no hay evaluaciones")
    void listarTodas_debeRetornarListaVacia_SiNoHayEvaluaciones() {
        when(evaluacionRepository.findAll()).thenReturn(List.of());

        List<EvaluacionResponseDTO> resultado = evaluacionService.listarTodas();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(evaluacionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId() retorna el DTO mapeado si la evaluacion existe")
    void buscarPorId_cuandoExiste_debeRetornarEvaluacionDTO() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));

        EvaluacionResponseDTO resultado = evaluacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Certamen 1", resultado.getNombre());
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId() lanza EvaluacionNotFoundException si el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarException() {
        when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EvaluacionNotFoundException.class, () -> evaluacionService.buscarPorId(99L));
        verify(evaluacionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("buscarPorTipo() retorna la lista de evaluaciones asociadas al tipo")
    void buscarPorTipo_debeRetornarListaDeEvaluaciones() {
        when(evaluacionRepository.findByTipo(1L)).thenReturn(List.of(evaluacion));

        List<EvaluacionResponseDTO> resultado = evaluacionService.buscarPorTipo(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(evaluacionRepository, times(1)).findByTipo(1L);
    }

    @Test
    @DisplayName("buscarPorTipo() lanza EvaluacionNotFoundException si no hay evaluaciones para el tipo")
    void buscarPorTipo_debeLanzarException_SiListaVacia() {
        when(evaluacionRepository.findByTipo(99L)).thenReturn(List.of());

        assertThrows(EvaluacionNotFoundException.class, () -> evaluacionService.buscarPorTipo(99L));
    }

    @Test
    @DisplayName("crear() guarda una nueva evaluacion cuando no hay duplicados")
    void crear_debeGuardarYRetornarDTO() {
        when(evaluacionRepository.existsByNombreIgnoreCase("Certamen 1")).thenReturn(false);
        when(tipoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(tipoCertamen));
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion);

        EvaluacionResponseDTO resultado = evaluacionService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Certamen 1", resultado.getNombre());
        verify(evaluacionRepository, times(1)).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("crear() lanza EvaluacionConflictException si el nombre ya existe")
    void crear_debeLanzarException_SiNombreYaExiste() {
        when(evaluacionRepository.existsByNombreIgnoreCase("Certamen 1")).thenReturn(true);

        assertThrows(EvaluacionConflictException.class, () -> evaluacionService.crear(requestDTO));
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("crear() lanza TipoEvaluacionNotFoundException si el tipo de evaluacion no existe")
    void crear_debeLanzarException_SiTipoNoExiste() {
        when(evaluacionRepository.existsByNombreIgnoreCase("Certamen 1")).thenReturn(false);
        when(tipoEvaluacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TipoEvaluacionNotFoundException.class, () -> evaluacionService.crear(requestDTO));
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("actualizar() modifica la evaluacion si existe y no hay conflicto")
    void actualizar_cuandoExiste_debeGuardarYRetornarDTO() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(tipoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(tipoCertamen));
        when(evaluacionRepository.existsByNameAndTipoExcludingId("Certamen 1", 1L, 1L)).thenReturn(false);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(evaluacion);

        EvaluacionResponseDTO resultado = evaluacionService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(evaluacionRepository, times(1)).findById(1L);
        verify(evaluacionRepository, times(1)).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("actualizar() lanza EvaluacionNotFoundException si la evaluacion no existe")
    void actualizar_cuandoNoExiste_debeLanzarException() {
        when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EvaluacionNotFoundException.class, () -> evaluacionService.actualizar(99L, requestDTO));
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("actualizar() lanza TipoEvaluacionNotFoundException si el tipo de evaluacion no existe")
    void actualizar_debeLanzarException_SiTipoNoExiste() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(tipoEvaluacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TipoEvaluacionNotFoundException.class, () -> evaluacionService.actualizar(1L, requestDTO));
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("actualizar() lanza EvaluacionConflictException si hay conflicto de nombre")
    void actualizar_debeLanzarException_SiHayConflicto() {
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(evaluacion));
        when(tipoEvaluacionRepository.findById(1L)).thenReturn(Optional.of(tipoCertamen));
        when(evaluacionRepository.existsByNameAndTipoExcludingId("Certamen 1", 1L, 1L)).thenReturn(true);

        assertThrows(EvaluacionConflictException.class, () -> evaluacionService.actualizar(1L, requestDTO));
        verify(evaluacionRepository, never()).save(any(Evaluacion.class));
    }

    @Test
    @DisplayName("eliminar() borra la evaluacion si el ID existe")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(evaluacionRepository.existsById(1L)).thenReturn(true);

        evaluacionService.eliminar(1L);

        verify(evaluacionRepository, times(1)).existsById(1L);
        verify(evaluacionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() lanza EvaluacionNotFoundException si el ID no existe")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(evaluacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(EvaluacionNotFoundException.class, () -> evaluacionService.eliminar(99L));
        verify(evaluacionRepository, never()).deleteById(any());
    }

}
