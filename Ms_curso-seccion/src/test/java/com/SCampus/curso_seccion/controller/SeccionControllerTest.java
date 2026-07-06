package com.SCampus.curso_seccion.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SCampus.curso_seccion.dto.CarreraAsignaturaResponseDTO;
import com.SCampus.curso_seccion.dto.SeccionResponseDTO;
import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.service.SeccionService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para SeccionController Enriquecido")
public class SeccionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SeccionService seccionService;

    @InjectMocks
    private SeccionController seccionController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private SeccionResponseDTO responseDTOMock;
    private Seccion seccionInputMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(seccionController).build();

        // 1. Instanciamos el DTO de integración simulado
        CarreraAsignaturaResponseDTO datosAcademicos = new CarreraAsignaturaResponseDTO();
        datosAcademicos.setIdCarreraAsignatura(1L);
        datosAcademicos.setNombreAsignatura("Desarrollo en Fullstack");
        datosAcademicos.setNombreSemestre("2026-1");

        // 2. Armamos el DTO de respuesta consolidado
        responseDTOMock = new SeccionResponseDTO();
        responseDTOMock.setId(5L);
        responseDTOMock.setNombre("Sección Alpha");
        responseDTOMock.setCursoId(12L);
        responseDTOMock.setDatosAcademicos(datosAcademicos);
        responseDTOMock.setFechaCreacion(LocalDateTime.now());

        // 3. Objeto Entidad para el body del POST
        seccionInputMock = new Seccion();
        seccionInputMock.setNombre("Sección Alpha");
    }

    @Test
    @DisplayName("GET /api/seccion - Debe retornar 200 con la lista de secciones enriquecidas")
    void obtenerSecciones_DebeRetornarStatus200YListaEnriquecida() throws Exception {
        when(seccionService.obtenerTodasEnriquecidas()).thenReturn(List.of(responseDTOMock));

        mockMvc.perform(get("/api/seccion")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].nombre").value("Sección Alpha"))
                .andExpect(jsonPath("$[0].datosAcademicos.nombreAsignatura").value("Desarrollo en Fullstack"));

        verify(seccionService, times(1)).obtenerTodasEnriquecidas();
    }

    @Test
    @DisplayName("GET /api/seccion/{id} - Debe retornar 200 y el DTO consolidado si existe")
    void obtenerSeccionPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        when(seccionService.obtenerPorIdEnriquecido(5L)).thenReturn(Optional.of(responseDTOMock));

        mockMvc.perform(get("/api/seccion/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.datosAcademicos.nombreSemestre").value("2026-1"));

        verify(seccionService, times(1)).obtenerPorIdEnriquecido(5L);
    }

    @Test
    @DisplayName("GET /api/seccion/{id} - Debe retornar 404 si el ID no se encuentra localmente")
    void obtenerSeccionPorId_DebeRetornarStatus404_CuandoIdNoExiste() throws Exception {
        when(seccionService.obtenerPorIdEnriquecido(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seccion/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(seccionService, times(1)).obtenerPorIdEnriquecido(99L);
    }

    @Test
    @DisplayName("POST /api/seccion - Debe retornar 201 y procesar el parámetro distribuido con éxito")
    void crear_DebeRetornarStatus201YSeccionEnriquecida() throws Exception {
        // Validamos pasándole el RequestParam implícito idCarreraVerificar = 1
        when(seccionService.guardarEnriquecido(any(Seccion.class), eq(1L))).thenReturn(responseDTOMock);

        mockMvc.perform(post("/api/seccion")
                .param("idCarreraVerificar", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seccionInputMock)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.datosAcademicos.nombreAsignatura").value("Desarrollo en Fullstack"));

        verify(seccionService, times(1)).guardarEnriquecido(any(Seccion.class), eq(1L));
    }

    @Test
    @DisplayName("DELETE /api/seccion/{id} - Debe retornar 204 si la remoción fue exitosa")
    void eliminar_DebeRetornarStatus204_CuandoIdExiste() throws Exception {
        when(seccionService.obtenerPorIdEnriquecido(5L)).thenReturn(Optional.of(responseDTOMock));

        mockMvc.perform(delete("/api/seccion/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(seccionService, times(1)).obtenerPorIdEnriquecido(5L);
        verify(seccionService, times(1)).eliminar(5L);
    }

    @Test
    @DisplayName("POST /api/seccion - Debe retornar 400 Bad Request si body es vacio")
    void crear_DebeRetornar400_CuandoBodyVacio() throws Exception {
        mockMvc.perform(post("/api/seccion")
                .param("idCarreraVerificar", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
