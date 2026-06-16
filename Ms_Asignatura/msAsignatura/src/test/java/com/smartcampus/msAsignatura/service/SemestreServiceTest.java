package com.smartcampus.msAsignatura.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

import com.smartcampus.msAsignatura.DTO.EstadoResponseDTO;
import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.client.EstadoClient;
import com.smartcampus.msAsignatura.model.Semestre;
import com.smartcampus.msAsignatura.repository.SemestreRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitario: SemestreService")
public class SemestreServiceTest {
    @Mock
    private SemestreRepository semestreRepository;

    @Mock
    private EstadoClient estadoClient;

    @InjectMocks
    private SemestreService semestreService;

    private Semestre sem;
    private EstadoResponseDTO estadoDTO;
    private SemestreRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        sem = new Semestre(1L, "2026-1", 1L);
        estadoDTO = new EstadoResponseDTO(); estadoDTO.setIdEstado(1L); estadoDTO.setNombre("ACTIVO");
        requestDTO = new SemestreRequestDTO(); requestDTO.setNombre("2026-2"); requestDTO.setIdEstado(1L);
    }

    @Test
    @DisplayName("listarTodosCronologicos() llama al query del repositorio y mapea los datos de respuesta con Feign")
    void listarTodosCronologicos_debeRetornarListaOrdenada() {
        when(semestreRepository.listarSemestreCronologicos()).thenReturn(List.of(sem));
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        List<SemestreResponseDTO> resultado = semestreService.listarTodosCronologicos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("2026-1", resultado.get(0).getNombre());
        verify(semestreRepository, times(1)).listarSemestreCronologicos();
    }

    @Test
    @DisplayName("buscarPorId() retorna el DTO mapeado si el semestre existe en la base de datos")
    void buscarPorId_cuandoExiste_debeRetornarSemestreDTO() {
        when(semestreRepository.findById(1L)).thenReturn(Optional.of(sem));
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        SemestreResponseDTO res = semestreService.buscarPorId(1L);
        assertNotNull(res);
        assertEquals("2026-1", res.getNombre());
        verify(semestreRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("buscarPorId() lanza RuntimeException si el ID buscado no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarException() {
        when(semestreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> semestreService.buscarPorId(99L));
    }

    @Test
    @DisplayName("crear() persiste un nuevo semestre en la BD y mapea la respuesta")
    void crear_debeGuardarYRetornarNuevoDTO() {
        when(semestreRepository.save(any(Semestre.class))).thenReturn(sem);
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        SemestreResponseDTO res = semestreService.crear(requestDTO);
        assertNotNull(res);
        verify(semestreRepository, times(1)).save(any(Semestre.class));
    }
    

    @Test
    @DisplayName("actualizar() modifica el semestre si findById localiza el ID actual")
    void actualizar_cuandoExiste_debeGuardarYRetornarDTO() {
        when(semestreRepository.findById(1L)).thenReturn(Optional.of(sem));
        when(semestreRepository.save(any(Semestre.class))).thenReturn(sem);
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        SemestreResponseDTO resultado = semestreService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(semestreRepository, times(1)).findById(1L);
        verify(semestreRepository, times(1)).save(any(Semestre.class));
    }

    @Test
    @DisplayName("actualizar() rebota con una RuntimeException si findById no encuentra el registro")
    void actualizar_cuandoNoExiste_debeLanzarException() {
        when(semestreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> semestreService.actualizar(99L, requestDTO));
        verify(semestreRepository, never()).save(any(Semestre.class));
    }

    @Test
    @DisplayName("eliminar() borra el semestre basándose en existsById de forma directa y autónoma")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(semestreRepository.existsById(1L)).thenReturn(true);

        semestreService.eliminar(1L);

        verify(semestreRepository, times(1)).existsById(1L);
        verify(semestreRepository, times(1)).deleteById(1L);
        verifyNoInteractions(estadoClient);
    }

    @Test
    @DisplayName("eliminar() arroja error controlado si existsById determina que el ID no existe")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(semestreRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> semestreService.eliminar(99L));
        verify(semestreRepository, never()).deleteById(any());
    }
}
