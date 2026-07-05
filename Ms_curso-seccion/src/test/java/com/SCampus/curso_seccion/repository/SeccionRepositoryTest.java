package com.SCampus.curso_seccion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SCampus.curso_seccion.model.Curso;
import com.SCampus.curso_seccion.model.Seccion;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para SeccionRepository")
public class SeccionRepositoryTest {

    @Mock
    private SeccionRepository seccionRepository;

    @Test
    @DisplayName("findByIdCurso() - Debe retornar secciones asociadas al ID numérico del curso")
    void findByIdCurso_DebeRetornarListaDeSecciones() {
        Curso curso = new Curso(1L, "14/06/26");
        
        Seccion seccion = new Seccion();
        seccion.setId(101L);
        seccion.setNombre("Sección Alpha");
        seccion.setCurso(curso);

        when(seccionRepository.findByCurso_Id(1L)).thenReturn(List.of(seccion));

        List<Seccion> resultado = seccionRepository.findByCurso_Id(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sección Alpha", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe localizar la sección omitiendo mayúsculas")
    void findByNombreIgnoreCase_DebeRetornarSeccion_CuandoExiste() {
        Curso curso = new Curso(1L, "14/06/26");

        Seccion seccion = new Seccion();
        seccion.setId(101L);
        seccion.setNombre("Matutina");
        seccion.setCurso(curso);

        when(seccionRepository.findByNombreIgnoreCase("MATUTINA")).thenReturn(Optional.of(seccion));

        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("MATUTINA");

        assertTrue(resultado.isPresent());
        assertEquals("Matutina", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() - Debe retornar vacío si el nombre no concuerda")
    void findByNombreIgnoreCase_DebeRetornarVacio_CuandoNoExiste() {
        when(seccionRepository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        Optional<Seccion> resultado = seccionRepository.findByNombreIgnoreCase("Inexistente");
        assertTrue(resultado.isEmpty());
    }
}
