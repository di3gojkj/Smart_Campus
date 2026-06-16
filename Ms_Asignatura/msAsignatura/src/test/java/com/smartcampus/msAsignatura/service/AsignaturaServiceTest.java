package com.smartcampus.msAsignatura.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.DTO.EstadoResponseDTO;
import com.smartcampus.msAsignatura.client.EstadoClient;
import com.smartcampus.msAsignatura.model.Asignatura;
import com.smartcampus.msAsignatura.repository.AsignaturaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitario: AsignaturaService")
public class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @Mock
    private EstadoClient estadoClient;

    @InjectMocks
    private AsignaturaService asignaturaService;

    private Asignatura asig;
    private EstadoResponseDTO estadoDTO;
    private AsignaturaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        asig = new Asignatura(1L, "Desarrollo en Fullstack", "INF-230", 1L);
        estadoDTO = new EstadoResponseDTO(); estadoDTO.setIdEstado(1L); estadoDTO.setNombre("ACTIVO");
        requestDTO = new AsignaturaRequestDTO(); requestDTO.setNombre("Desarrollo en Fullstack Avanzado"); requestDTO.setSigla("INF-230"); requestDTO.setIdEstado(1L);
    }

    @Test
    @DisplayName("listarTodas() obtiene las asignaturas con findAll y mapea sus nombres de estado")
    void listarTodas_debeRetornarListaDeAsignaturasConEstado() {
        when(asignaturaRepository.findAll()).thenReturn(List.of(asig));
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        List<AsignaturaResponseDTO> resultado = asignaturaService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombreEstado());
        verify(asignaturaRepository, times(1)).findAll();
        verify(estadoClient, times(1)).obtenerEstadoPorId(1L);
    }

    @Test
    @DisplayName("buscarPorId retorna el DTO mapeado si la asignatura existe en la base de datos")
    void buscarPorId_cuandoExiste_debeRetornarAsignaturaDTO() {
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asig));
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        AsignaturaResponseDTO res = asignaturaService.buscarPorId(1L);
        assertNotNull(res);
        assertEquals("Desarrollo en Fullstack", res.getNombre());
        verify(asignaturaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId lanza RuntimeException si el ID solicitado no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarException() {
        when(asignaturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> asignaturaService.buscarPorId(99L));
    }
    @Test
    @DisplayName("crear guarda una nueva asignatura usando save() y resuelve el estado con Feign")
    void crear_debeGuardarYRetornarNuevoDTO() {
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asig);
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        AsignaturaResponseDTO res = asignaturaService.crear(requestDTO);
        assertNotNull(res);
        verify(asignaturaRepository, times(1)).save(any(Asignatura.class));
    }

    @Test
    @DisplayName("actualizar() modifica la asignatura si findById la encuentra en la BD")
    void actualizar_cuandoExiste_debeGuardarYRetornarDTO() {
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asig));
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asig);
        when(estadoClient.obtenerEstadoPorId(1L)).thenReturn(estadoDTO);

        AsignaturaResponseDTO resultado = asignaturaService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(asignaturaRepository, times(1)).findById(1L);
        verify(asignaturaRepository, times(1)).save(any(Asignatura.class));
    }

    @Test
    @DisplayName("actualizar() lanza una RuntimeException si findById retorna un Optional vacio")
    void actualizar_cuandoNoExiste_debeLanzarException() {
        when(asignaturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> asignaturaService.actualizar(99L, requestDTO));
        verify(asignaturaRepository, never()).save(any(Asignatura.class));
    }

    @Test
    @DisplayName("eliminar borra el registro por ID si existsById confirma que existe sin invocar a estadoClient")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(asignaturaRepository.existsById(1L)).thenReturn(true);

        asignaturaService.eliminar(1L);

        verify(asignaturaRepository, times(1)).existsById(1L);
        verify(asignaturaRepository, times(1)).deleteById(1L);
        verifyNoInteractions(estadoClient);
    }

    @Test
    @DisplayName("eliminar() lanza una RuntimeException si existsById es false")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(asignaturaRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> asignaturaService.eliminar(99L));
        verify(asignaturaRepository, never()).deleteById(any());
    }

}
