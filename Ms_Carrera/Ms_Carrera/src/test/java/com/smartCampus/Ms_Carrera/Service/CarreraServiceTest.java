package com.smartCampus.Ms_Carrera.Service;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Exception.CarreraConflictException;
import com.smartCampus.Ms_Carrera.Exception.CarreraNotFoundException;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test de Integracion: CarreraService")
public class CarreraServiceTest {

    @Mock
    private CarreraRepository carreraRepository;

    @InjectMocks
    private CarreraService carreraService;

    private Carrera carrera;
    private CarreraRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        carrera = new Carrera(1L, "Ingenieria en Informatica", "INF-001", 1L);
        requestDTO = new CarreraRequestDTO();
        requestDTO.setNombre("Ingenieria en Informatica");
        requestDTO.setSigla("INF-001");
        requestDTO.setIdEstado(1L);
    }

    @Test
    @DisplayName("listarTodas() retorna la lista de DTO de todas las carreras")
    void listarTodas_debeRetornarListaDeCarreras() {
        when(carreraRepository.findAll()).thenReturn(List.of(carrera));

        List<CarreraResponseDTO> resultado = carreraService.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ingenieria en Informatica", resultado.get(0).getNombre());
        verify(carreraRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listarTodas() retorna lista vacia cuando no hay carreras")
    void listarTodas_debeRetornarListaVacia_SiNoHayCarreras() {
        when(carreraRepository.findAll()).thenReturn(List.of());

        List<CarreraResponseDTO> resultado = carreraService.listarTodas();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(carreraRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId() retorna el DTO mapeado si la carrera existe")
    void buscarPorId_cuandoExiste_debeRetornarCarreraDTO() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));

        CarreraResponseDTO resultado = carreraService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Ingenieria en Informatica", resultado.getNombre());
        verify(carreraRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId() lanza CarreraNotFoundException si el ID no existe")
    void buscarPorId_cuandoNoExiste_debeLanzarException() {
        when(carreraRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> carreraService.buscarPorId(99L));
        verify(carreraRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("crear() guarda una nueva carrera cuando la sigla no existe")
    void crear_debeGuardarYRetornarDTO() {
        when(carreraRepository.findBySigla("INF-001")).thenReturn(Optional.empty());
        when(carreraRepository.save(any(Carrera.class))).thenReturn(carrera);

        CarreraResponseDTO resultado = carreraService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Ingenieria en Informatica", resultado.getNombre());
        verify(carreraRepository, times(1)).findBySigla("INF-001");
        verify(carreraRepository, times(1)).save(any(Carrera.class));
    }

    @Test
    @DisplayName("crear() lanza CarreraConflictException si la sigla ya existe")
    void crear_debeLanzarConflict_SiSiglaYaExiste() {
        when(carreraRepository.findBySigla("INF-001")).thenReturn(Optional.of(carrera));

        assertThrows(CarreraConflictException.class, () -> carreraService.crear(requestDTO));
        verify(carreraRepository, never()).save(any(Carrera.class));
    }

    @Test
    @DisplayName("actualizar() modifica la carrera si existe y la sigla no genera conflicto")
    void actualizar_cuandoExiste_debeGuardarYRetornarDTO() {
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(carreraRepository.findBySiglaExcludingCurrent("INF-001", 1L)).thenReturn(Optional.empty());
        when(carreraRepository.save(any(Carrera.class))).thenReturn(carrera);

        CarreraResponseDTO resultado = carreraService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(carreraRepository, times(1)).findById(1L);
        verify(carreraRepository, times(1)).save(any(Carrera.class));
    }

    @Test
    @DisplayName("actualizar() lanza CarreraNotFoundException si el ID no existe")
    void actualizar_cuandoNoExiste_debeLanzarException() {
        when(carreraRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> carreraService.actualizar(99L, requestDTO));
        verify(carreraRepository, never()).save(any(Carrera.class));
    }

    @Test
    @DisplayName("actualizar() lanza CarreraConflictException si la sigla ya pertenece a otra carrera")
    void actualizar_debeLanzarConflict_SiSiglaYaExisteEnOtraCarrera() {
        Carrera otraCarrera = new Carrera(2L, "Mecanica Automotriz", "INF-001", 1L);
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(carreraRepository.findBySiglaExcludingCurrent("INF-001", 1L)).thenReturn(Optional.of(otraCarrera));

        assertThrows(CarreraConflictException.class, () -> carreraService.actualizar(1L, requestDTO));
        verify(carreraRepository, never()).save(any(Carrera.class));
    }

    @Test
    @DisplayName("eliminar() borra la carrera si el ID existe")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(carreraRepository.existsById(1L)).thenReturn(true);

        carreraService.eliminar(1L);

        verify(carreraRepository, times(1)).existsById(1L);
        verify(carreraRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() lanza CarreraNotFoundException si el ID no existe")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(carreraRepository.existsById(99L)).thenReturn(false);

        assertThrows(CarreraNotFoundException.class, () -> carreraService.eliminar(99L));
        verify(carreraRepository, never()).deleteById(any());
    }


}
