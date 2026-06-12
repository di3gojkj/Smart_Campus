package com.smartCampus.Ms_Carrera.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test UNIT de CarreraService")
public class CarreraServiceTest {

    @Mock
    private CarreraRepository carreraRepository;

    @InjectMocks
    private CarreraService carreraService;

    private Carrera carreraEjemplo;
    private CarreraRequestDTO dtoEjemplo;

    @BeforeEach
    void setUp() {
        carreraEjemplo = new Carrera(1L, "Ingenieria en Informatica", "INF-001", 1L);
        dtoEjemplo = new CarreraRequestDTO("Ingenieria en Informatica","INF-001",1L);
    }

    @Test
    @DisplayName("FindAll() retorna la lista de DTO de todas las carreras")
    void findAll_debeRetornarListaDeCarreras() {
        when(carreraRepository.findAll()).thenReturn(List.of(carreraEjemplo));

        List<CarreraResponseDTO> resultado = carreraService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ingenieria en Informatica", resultado.get(0).getNombre());

        verify(carreraRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll() debe retornar lista vacia cuando no hay carreras en MySQL")
    void findAll_debeRetornarListaVacia_SiNoHayCarreras() {
        when(carreraRepository.findAll()).thenReturn(List.of());

        List<CarreraResponseDTO> resultado = carreraService.listarTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(carreraRepository, times(1)).findAll();
    }
}
