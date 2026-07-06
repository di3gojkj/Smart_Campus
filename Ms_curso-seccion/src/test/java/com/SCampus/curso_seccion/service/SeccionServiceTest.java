package com.SCampus.curso_seccion.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.client.CarreraAsignaturaClient;
import com.SCampus.curso_seccion.dto.CarreraAsignaturaResponseDTO;
import com.SCampus.curso_seccion.dto.SeccionResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.repository.CursoRepository;
import com.SCampus.curso_seccion.repository.SeccionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias Integradas para SeccionService (Con Mock OpenFeign)")
public class SeccionServiceTest {

    @Mock
    private SeccionRepository seccionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CarreraAsignaturaClient carreraAsignaturaClient; // 🛠️ Mock de la integración remota

    @InjectMocks
    private SeccionService seccionService;

    private Curso cursoMock;
    private Seccion seccionEjemplo;
    private CarreraAsignaturaResponseDTO relacionMock;

    @BeforeEach
    void setUp() {
        cursoMock = new Curso();
        cursoMock.setId(45L);
        cursoMock.setFechaCreacion("14/06/26");

        seccionEjemplo = new Seccion();
        seccionEjemplo.setId(101L);
        seccionEjemplo.setNombre("Sección B");
        seccionEjemplo.setCurso(cursoMock);

        // Instanciamos la respuesta simulada que viene de Ms_Carrera
        relacionMock = new CarreraAsignaturaResponseDTO();
        relacionMock.setIdCarreraAsignatura(1L);
        relacionMock.setIdCarrera(1L);
        relacionMock.setIdAsignatura(5L);
        relacionMock.setNombreAsignatura("Desarrollo en Fullstack");
        relacionMock.setNombreSemestre("2026-1");
    }

    @Test
    @DisplayName("obtenerTodasEnriquecidas() - Debe retornar DTOs con la metadata externa inyectada")
    void obtenerTodasEnriquecidas_DebeRetornarPayloadConsolidado() {
        // Arrange
        when(seccionRepository.findAll()).thenReturn(List.of(seccionEjemplo));
        when(carreraAsignaturaClient.listarPorCarrera(1L)).thenReturn(List.of(relacionMock));

        // Act
        List<SeccionResponseDTO> resultado = seccionService.obtenerTodasEnriquecidas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sección B", resultado.get(0).getNombre());
        assertNotNull(resultado.get(0).getDatosAcademicos());
        assertEquals("Desarrollo en Fullstack", resultado.get(0).getDatosAcademicos().getNombreAsignatura());
        
        verify(seccionRepository, times(1)).findAll();
        verify(carreraAsignaturaClient, times(1)).listarPorCarrera(1L);
    }

    @Test
    @DisplayName("obtenerTodasEnriquecidas() - Debe ser resiliente si Ms_Carrera falla de fondo")
    void obtenerTodasEnriquecidas_DebeRetornarDatosLocales_CuandoFallaClienteFeign() {
        // Arrange
        when(seccionRepository.findAll()).thenReturn(List.of(seccionEjemplo));
        when(carreraAsignaturaClient.listarPorCarrera(1L)).thenThrow(new RuntimeException("Timeout HTTP"));

        // Act
        List<SeccionResponseDTO> resultado = seccionService.obtenerTodasEnriquecidas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertNull(resultado.get(0).getDatosAcademicos()); // Resiliencia: La sección se entrega pero sin datos externos
        
        verify(seccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardarEnriquecido() - Debe almacenar con éxito si las validaciones local y remota pasan")
    void guardarEnriquecido_DebeGuardar_CuandoCursoYCarreraExisten() {
        // Arrange
        when(cursoRepository.existsById(45L)).thenReturn(true);
        when(carreraAsignaturaClient.listarPorCarrera(1L)).thenReturn(List.of(relacionMock));
        when(seccionRepository.save(any(Seccion.class))).thenReturn(seccionEjemplo);

        // Act
        SeccionResponseDTO resultado = seccionService.guardarEnriquecido(seccionEjemplo, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(101L, resultado.getId());
        assertNotNull(resultado.getDatosAcademicos());
        assertEquals("Desarrollo en Fullstack", resultado.getDatosAcademicos().getNombreAsignatura());

        verify(cursoRepository, times(1)).existsById(45L);
        verify(carreraAsignaturaClient, times(2)).listarPorCarrera(1L); // Una para validar, otra para mapear
        verify(seccionRepository, times(1)).save(any(Seccion.class));
    }

    @Test
    @DisplayName("guardarEnriquecido() - Debe lanzar excepción si la carrera remota no posee asignaturas asignadas")
    void guardarEnriquecido_DebeLanzarExcepcion_CuandoCarreraRemotaEstaVacia() {
        // Arrange
        when(cursoRepository.existsById(45L)).thenReturn(true);
        when(carreraAsignaturaClient.listarPorCarrera(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            seccionService.guardarEnriquecido(seccionEjemplo, 1L);
        });

        verify(cursoRepository, times(1)).existsById(45L);
        verify(carreraAsignaturaClient, times(1)).listarPorCarrera(1L);
        verify(seccionRepository, never()).save(any(Seccion.class));
    }

    @Test
    @DisplayName("obtenerPorIdEnriquecido() - Debe retornar el DTO consolidado si el identificador existe localmente")
    void obtenerPorIdEnriquecido_DebeRetornarDTO_CuandoIdExiste() {
        // Arrange
        when(seccionRepository.findById(101L)).thenReturn(Optional.of(seccionEjemplo));
        when(carreraAsignaturaClient.listarPorCarrera(1L)).thenReturn(List.of(relacionMock));

        // Act
        Optional<SeccionResponseDTO> resultado = seccionService.obtenerPorIdEnriquecido(101L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Sección B", resultado.get().getNombre());
        assertEquals("Desarrollo en Fullstack", resultado.get().getDatosAcademicos().getNombreAsignatura());
        
        verify(seccionRepository, times(1)).findById(101L);
    }

    @Test
    @DisplayName("obtenerPorIdEnriquecido() - Debe retornar Optional vacío cuando el ID no existe localmente")
    void obtenerPorIdEnriquecido_DebeRetornarVacio_CuandoIdNoExiste() {
        when(seccionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<SeccionResponseDTO> resultado = seccionService.obtenerPorIdEnriquecido(99L);

        assertTrue(resultado.isEmpty());
        verify(seccionRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("guardarEnriquecido() - Debe lanzar excepción si el Curso no existe")
    void guardarEnriquecido_DebeLanzarExcepcion_CuandoCursoNoExiste() {
        // Simulamos que el curso base no existe
        when(cursoRepository.existsById(45L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            seccionService.guardarEnriquecido(seccionEjemplo, 1L);
        });

        verify(cursoRepository, times(1)).existsById(45L);
        verify(carreraAsignaturaClient, never()).listarPorCarrera(any());
        verify(seccionRepository, never()).save(any(Seccion.class));
    }

    @Test
    @DisplayName("eliminar() - Debe llamar al repositorio para eliminar por ID")
    void eliminar_DebeLlamarAlRepositorio() {
        // Ejecutamos el método que no retorna nada (void)
        seccionService.eliminar(5L);
        
        // Verificamos que el repositorio efectivamente fue llamado para borrar
        verify(seccionRepository, times(1)).deleteById(5L);
    }
}

