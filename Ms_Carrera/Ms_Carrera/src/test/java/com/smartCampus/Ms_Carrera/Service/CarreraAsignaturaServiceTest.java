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

import com.smartCampus.Ms_Carrera.Client.AsignaturaClient;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Exception.CarreraAsignaturaConflictException;
import com.smartCampus.Ms_Carrera.Exception.CarreraAsignaturaNotFoundException;
import com.smartCampus.Ms_Carrera.Exception.CarreraNotFoundException;
import com.smartCampus.Ms_Carrera.Repository.CarreraAsignaturaRepository;
import com.smartCampus.Ms_Carrera.Repository.CarreraRepository;
import com.smartCampus.Ms_Carrera.model.Carrera;
import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;




@ExtendWith(MockitoExtension.class)
@DisplayName("Test de Integracion: CarreraAsignaturaService")
public class CarreraAsignaturaServiceTest {

    @Mock
    private CarreraAsignaturaRepository repository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private AsignaturaClient asignaturaClient;

    @InjectMocks
    private CarreraAsignaturaService carreraAsignaturaService;

    private Carrera carrera;
    private CarreraAsignatura relacion;
    private CarreraAsignaturaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        carrera = new Carrera(1L, "Ingenieria en Informatica", "INF-001", 1L);
        relacion = new CarreraAsignatura(1L, carrera, 3L, 1L);
        requestDTO = new CarreraAsignaturaRequestDTO(1L, 3L, 1L);
    }

    @Test
    @DisplayName("listarTodas() retorna la lista de relaciones de una carrera")
    void listarTodas_debeRetornarListaDeRelaciones() {
        when(repository.findByCarrera_IdCarrera(1L)).thenReturn(List.of(relacion));
        when(asignaturaClient.obtenerAsignaturaPorId(3L)).thenReturn(null);

        List<CarreraAsignaturaResponseDTO> resultado = carreraAsignaturaService.listarTodas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getIdAsignatura());
        verify(repository, times(1)).findByCarrera_IdCarrera(1L);
    }

    @Test
    @DisplayName("listarTodas() retorna lista vacia cuando no hay relaciones")
    void listarTodas_debeRetornarListaVacia_SiNoHayRelaciones() {
        when(repository.findByCarrera_IdCarrera(1L)).thenReturn(List.of());

        List<CarreraAsignaturaResponseDTO> resultado = carreraAsignaturaService.listarTodas(1L);

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(repository, times(1)).findByCarrera_IdCarrera(1L);
    }

    @Test
    @DisplayName("crear() guarda una nueva relacion cuando no existe duplicado")
    void crear_debeGuardarYRetornarDTO() {
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(1L, 3L, 1L)).thenReturn(false);
        when(carreraRepository.findById(1L)).thenReturn(Optional.of(carrera));
        when(repository.save(any(CarreraAsignatura.class))).thenReturn(relacion);
        when(asignaturaClient.obtenerAsignaturaPorId(3L)).thenReturn(null);

        CarreraAsignaturaResponseDTO resultado = carreraAsignaturaService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals(3L, resultado.getIdAsignatura());
        verify(repository, times(1)).save(any(CarreraAsignatura.class));
    }

    @Test
    @DisplayName("crear() lanza CarreraAsignaturaConflictException si la relacion ya existe")
    void crear_debeLanzarConflict_SiRelacionYaExiste() {
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(1L, 3L, 1L)).thenReturn(true);

        assertThrows(CarreraAsignaturaConflictException.class, () -> carreraAsignaturaService.crear(requestDTO));
        verify(repository, never()).save(any(CarreraAsignatura.class));
    }

    @Test
    @DisplayName("crear() lanza CarreraNotFoundException si la carrera no existe")
    void crear_debeLanzarNotFoundException_SiCarreraNoExiste() {
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(1L, 3L, 1L)).thenReturn(false);
        when(carreraRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> carreraAsignaturaService.crear(requestDTO));
        verify(repository, never()).save(any(CarreraAsignatura.class));
    }

    @Test
    @DisplayName("actualizar() modifica la relacion exitosamente")
    void actualizar_debeModificarYRetornarDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(relacion));
        when(repository.save(any(CarreraAsignatura.class))).thenReturn(relacion);
        when(asignaturaClient.obtenerAsignaturaPorId(3L)).thenReturn(null);

        CarreraAsignaturaResponseDTO resultado = carreraAsignaturaService.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(CarreraAsignatura.class));
    }

    @Test
    @DisplayName("actualizar() lanza CarreraAsignaturaNotFoundException si el ID no existe")
    void actualizar_debeLanzarNotFound_SiIdNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CarreraAsignaturaNotFoundException.class, () -> carreraAsignaturaService.actualizar(99L, requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar() lanza CarreraAsignaturaConflictException si hay cambio de identidad y ya existe el duplicado")
    void actualizar_debeLanzarConflict_SiNuevosDatosYaExisten() {
        when(repository.findById(1L)).thenReturn(Optional.of(relacion));
        
        com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO dtoConCambio = 
            new com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO(2L, 4L, 2L);
            
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(2L, 4L, 2L)).thenReturn(true);

        assertThrows(CarreraAsignaturaConflictException.class, () -> 
            carreraAsignaturaService.actualizar(1L, dtoConCambio)
        );
    }

    
    @Test
    @DisplayName("actualizar() cambia la carrera exitosamente si los IDs son distintos")
    void actualizar_debeCambiarCarrera_CuandoIdCarreraEsDiferente() {
        when(repository.findById(1L)).thenReturn(Optional.of(relacion));
        
        
        CarreraAsignaturaRequestDTO dtoNuevaCarrera = new CarreraAsignaturaRequestDTO(2L, 3L, 1L);
        Carrera nuevaCarreraObjeto = new Carrera(2L, "Ingeniería Civil", "CIV-002", 1L);
        
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(2L, 3L, 1L)).thenReturn(false);
        when(carreraRepository.findById(2L)).thenReturn(Optional.of(nuevaCarreraObjeto));
        when(repository.save(any(CarreraAsignatura.class))).thenReturn(relacion);

        CarreraAsignaturaResponseDTO resultado = carreraAsignaturaService.actualizar(1L, dtoNuevaCarrera);

        assertNotNull(resultado);
        verify(carreraRepository, times(1)).findById(2L);
        verify(repository, times(1)).save(any(CarreraAsignatura.class));
    }

    
    @Test
    @DisplayName("actualizar() lanza CarreraNotFoundException si la nueva carrera no existe")
    void actualizar_debeLanzarCarreraNotFound_CuandoNuevaCarreraNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(relacion));
        
        CarreraAsignaturaRequestDTO dtoNuevaCarrera = new CarreraAsignaturaRequestDTO(2L, 3L, 1L);
        
        when(repository.existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(2L, 3L, 1L)).thenReturn(false);
        when(carreraRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CarreraNotFoundException.class, () -> 
            carreraAsignaturaService.actualizar(1L, dtoNuevaCarrera)
        );
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("toResponseDTO lanza catch cuando falla el Feign Client")
    void toResponseDTO_debeManejarExcepcionDeFeingClient() {
        when(repository.findByCarrera_IdCarrera(1L)).thenReturn(List.of(relacion));
        
        when(asignaturaClient.obtenerAsignaturaPorId(3L)).thenThrow(new RuntimeException("Error de conexion"));

        List<CarreraAsignaturaResponseDTO> resultado = carreraAsignaturaService.listarTodas(1L);

        assertNotNull(resultado);
        assertEquals("Nombre no disponible", resultado.get(0).getNombreAsignatura());
    }

    @Test
    @DisplayName("eliminar() borra la relacion si el ID existe")
    void eliminar_cuandoExiste_debeEjecutarElBorrado() {
        when(repository.existsById(1L)).thenReturn(true);

        carreraAsignaturaService.eliminar(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() lanza CarreraAsignaturaNotFoundException si el ID no existe")
    void eliminar_cuandoNoExiste_debeLanzarException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(CarreraAsignaturaNotFoundException.class, () -> carreraAsignaturaService.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}