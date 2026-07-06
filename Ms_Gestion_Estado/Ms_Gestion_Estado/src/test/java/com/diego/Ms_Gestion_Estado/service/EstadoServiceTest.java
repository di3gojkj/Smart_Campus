package com.diego.Ms_Gestion_Estado.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.exception.EstadoNotFoundException;
import com.diego.Ms_Gestion_Estado.model.Estado;
import com.diego.Ms_Gestion_Estado.repository.EstadoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unitario de EstadoService")
public class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @InjectMocks
    private EstadoService estadoService;

    private Estado estadoEjemplo;
    private EstadoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        estadoEjemplo = new Estado(1L, "ACTIVO");
        requestDTO = new EstadoRequestDTO("ACTIVO");
    }

    @Test
    @DisplayName("obtenerTodos() retorna lista de DTOs")
    void obtenerTodos_debeRetornarLista() {
        when(estadoRepository.findAll()).thenReturn(List.of(estadoEjemplo));
        List<EstadoResponseDTO> resultado = estadoService.obtenerTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("obtenerPorId() retorna el DTO correcto cuando existe")
    void obtenerPorId_debeRetornarDto_cuandoExiste() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEjemplo));
        EstadoResponseDTO resultado = estadoService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdEstado());
    }

    @Test
    @DisplayName("obtenerPorId() lanza EstadoNotFoundException cuando no existe")
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(estadoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EstadoNotFoundException.class, () -> estadoService.obtenerPorId(99L));
    }

    @Test
    @DisplayName("guardar() crea y retorna el estado DTO si el nombre no existe")
    void guardar_debeRetornarEstadoCreado_cuandoNombreNoExiste() {
        // Simulamos que el nombre NO existe
        when(estadoRepository.findByNombreIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(estadoRepository.save(any(Estado.class))).thenReturn(estadoEjemplo);

        EstadoResponseDTO resultado = estadoService.guardar(requestDTO);
        assertEquals("ACTIVO", resultado.getNombre());
        verify(estadoRepository, times(1)).save(any(Estado.class));
    }

    @Test
    @DisplayName("guardar() lanza RuntimeException si el nombre ya existe")
    void guardar_debeLanzarExcepcion_cuandoNombreYaExiste() {
        // Simulamos que el nombre YA existe en la BD
        when(estadoRepository.findByNombreIgnoreCase(requestDTO.getNombre())).thenReturn(Optional.of(estadoEjemplo));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadoService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("Ya existe un estado"));
        verify(estadoRepository, never()).save(any(Estado.class)); // Asegura que no se guarde
    }

    @Test
    @DisplayName("guardar() lanza excepcion si ocurre un error inesperado al guardar en BD")
    void guardar_debeLanzarExcepcion_cuandoFallaBD() {
        // Simulamos que el nombre no existe, por lo que intenta guardar
        when(estadoRepository.findByNombreIgnoreCase(anyString())).thenReturn(Optional.empty());
        
        // Simulamos que la base de datos explota en el proceso
        when(estadoRepository.save(any(Estado.class))).thenThrow(new RuntimeException("Error interno de BD"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> estadoService.guardar(requestDTO));
        assertTrue(ex.getMessage().contains("Error interno de BD"));
    }

    
}