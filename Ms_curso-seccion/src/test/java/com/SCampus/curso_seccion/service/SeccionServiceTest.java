package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.repository.CursoRepository;
import com.SCampus.curso_seccion.repository.SeccionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de SeccionService")
public class SeccionServiceTest {

    @Mock
    private SeccionRepository seccionRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private SeccionService seccionService;

    private Seccion seccionEjemplo;

    @BeforeEach
    void setUp() {
        seccionEjemplo = new Seccion(5L, "Sección A", 12L);
    }

    @Test
    @DisplayName("obtenerTodas() retorna el listado de todas las secciones académicas")
    void obtenerTodas_debeRetornarListaDeSecciones() {
        when(seccionRepository.findAll()).thenReturn(List.of(seccionEjemplo));

        List<Seccion> resultado = seccionService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sección A", resultado.get(0).getNombre());
        verify(seccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId() retorna un Optional con la sección cuando el ID coincide")
    void obtenerPorId_debeRetornarSeccion_cuandoIdExiste() {
        when(seccionRepository.findById(5L)).thenReturn(Optional.of(seccionEjemplo));

        Optional<Seccion> resultado = seccionService.obtenerPorId(5L);

        assertTrue(resultado.isPresent());
        assertEquals("Sección A", resultado.get().getNombre());
        verify(seccionRepository, times(1)).findById(5L);
    }

    @Test
    @DisplayName("guardar() almacena con éxito la sección si el ID de curso existe")
    void guardar_debeAlmacenarSeccion_cuandoCursoPadreExiste() {
        when(cursoRepository.existsById(12L)).thenReturn(true);
        when(seccionRepository.save(any(Seccion.class))).thenReturn(seccionEjemplo);

        Seccion resultado = seccionService.guardar(seccionEjemplo);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals(12L, resultado.getIdCurso());
        verify(cursoRepository, times(1)).existsById(12L);
        verify(seccionRepository, times(1)).save(any(Seccion.class));
    }

    @Test
    @DisplayName("guardar() debe lanzar RuntimeException si se asigna un ID de curso que no existe")
    void guardar_debeLanzarExcepcion_cuandoCursoPadreNoExiste() {
        when(cursoRepository.existsById(12L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            seccionService.guardar(seccionEjemplo);
        });

        verify(cursoRepository, times(1)).existsById(12L);
        verify(seccionRepository, never()).save(any(Seccion.class));
    }

    @Test
    @DisplayName("eliminar() remueve el registro físico de la base de datos a partir de su ID")
    void eliminar_debeEjecutarBorrado() {
        seccionService.eliminar(5L);

        verify(seccionRepository, times(1)).deleteById(5L);
    }
}
