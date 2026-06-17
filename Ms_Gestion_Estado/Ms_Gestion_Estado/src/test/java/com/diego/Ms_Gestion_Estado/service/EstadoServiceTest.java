package com.diego.Ms_Gestion_Estado.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        estadoEjemplo = new Estado(1L, "ACTIVO");
    }

    @Test
    @DisplayName("obtenerTodos() retorna lista de DTOs")
    void obtenerTodos_debeRetornarLista() {
        when(estadoRepository.findAll()).thenReturn(List.of(estadoEjemplo));

        List<EstadoResponseDTO> resultado = estadoService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("obtenerPorId() retorna el DTO correcto")
    void obtenerPorId_debeRetornarDto() {
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estadoEjemplo));

        EstadoResponseDTO resultado = estadoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdEstado());
    }

    @Test
    @DisplayName("guardar() crea y retorna el estado DTO")
    void guardar_debeRetornarEstadoCreado() {
        EstadoRequestDTO request = new EstadoRequestDTO("INACTIVO");
        Estado estadoInactivo = new Estado(2L, "INACTIVO");

        when(estadoRepository.save(any(Estado.class))).thenReturn(estadoInactivo);

        EstadoResponseDTO resultado = estadoService.guardar(request);

        assertEquals("INACTIVO", resultado.getNombre());
        verify(estadoRepository, times(1)).save(any(Estado.class));
    }
}