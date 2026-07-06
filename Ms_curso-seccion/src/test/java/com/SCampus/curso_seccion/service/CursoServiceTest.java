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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoService")
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso cursoEjemplo;
    private CursoRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        cursoEjemplo = new Curso();
        cursoEjemplo.setId(12L);
        cursoEjemplo.setFechaCreacion("14/06/26");

        requestDto = new CursoRequestDTO();
        requestDto.setNombre("Programación");
        requestDto.setFechaCreacion("14/06/26");
    }

    @Test
    @DisplayName("obtenerTodos() - Debe retornar el listado de DTOs correspondientes")
    void obtenerTodos_DebeRetornarListaDeCursos() {
        when(cursoRepository.findAll()).thenReturn(List.of(cursoEjemplo));

        List<CursoResponseDTO> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("14/06/26", resultado.get(0).getFechaCreacion());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardarCurso() - Debe registrar exitosamente si la fecha es nueva en BD")
    void guardarCurso_DebeAlmacenar_CuandoFechaEsUnica() {
        when(cursoRepository.findByFechaCreacion("14/06/26")).thenReturn(Optional.empty());
        when(cursoRepository.save(any(Curso.class))).thenReturn(cursoEjemplo);

        CursoResponseDTO resultado = cursoService.guardarCurso(requestDto);

        assertNotNull(resultado);
        assertEquals(12L, resultado.getId());
        assertEquals("14/06/26", resultado.getFechaCreacion());
        verify(cursoRepository, times(1)).findByFechaCreacion("14/06/26");
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("guardarCurso() - Debe lanzar RuntimeException si la fecha ya se encuentra registrada")
    void guardarCurso_DebeLanzarExcepcion_CuandoFechaDuplicada() {
        when(cursoRepository.findByFechaCreacion("14/06/26")).thenReturn(Optional.of(cursoEjemplo));

        assertThrows(RuntimeException.class, () -> {
            cursoService.guardarCurso(requestDto);
        });

        verify(cursoRepository, times(1)).findByFechaCreacion("14/06/26");
        verify(cursoRepository, never()).save(any(Curso.class));
    }

    @Test
    @DisplayName("obtenerPorId() - Debe retornar un Optional con el DTO mapeado si el ID existe")
    void obtenerPorId_DebeRetornarCursoDto_CuandoIdExiste() {
        when(cursoRepository.findById(12L)).thenReturn(Optional.of(cursoEjemplo));

        Optional<CursoResponseDTO> resultado = cursoService.obtenerPorId(12L);

        assertTrue(resultado.isPresent());
        assertEquals(12L, resultado.get().getId());
        assertEquals("14/06/26", resultado.get().getFechaCreacion());
        verify(cursoRepository, times(1)).findById(12L);
    }
    
    @Test
    @DisplayName("obtenerPorId() - Debe retornar Optional vacío cuando el ID no existe")
    void obtenerPorId_DebeRetornarVacio_CuandoIdNoExiste() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CursoResponseDTO> resultado = cursoService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(cursoRepository, times(1)).findById(99L);
    }
}
