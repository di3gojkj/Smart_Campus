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

import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;

import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test UNIT de CarreraAsignaturaService")
public class CarreraAsignaturaServiceTest {

    @Mock
    private CarreraAsignaturaRepository carreraAsignaturaRepository;

    @InjectMocks
    private CarreraAsignaturaService carreraAsignaturaService;

    private CarreraAsignatura entidadEjemplo;

    @BeforeEach
    void setUp() {
        Carrera carrera = new Carrera(1L, "Ingenieria en Informatica", "INF-001", 1L);
        entidadEjemplo = new CarreraAsignatura(1L, carrera, 3L, 1L);
    }

    @Test
    @DisplayName("FindAll() retorna la lista de DTO de todas las relaciones")
    void findAll_debeRetornarListaDeRelaciones() {
        when(carreraAsignaturaRepository.findAll()).thenReturn(List.of(entidadEjemplo));

        List<CarreraAsignaturaResponseDTO> resultado = carreraAsignaturaService.listarTodas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getIdAsignatura());
        verify(carreraAsignaturaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll() debe retornar lista vacia cuando no hay relaciones")
    void findAll_debeRetornarListaVacia_SiNoHayRelaciones() {
        when(carreraAsignaturaRepository.findAll()).thenReturn(List.of());

        List<CarreraAsignaturaResponseDTO> resultado = carreraAsignaturaService.listarTodas(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(carreraAsignaturaRepository, times(1)).findAll();
    }
}