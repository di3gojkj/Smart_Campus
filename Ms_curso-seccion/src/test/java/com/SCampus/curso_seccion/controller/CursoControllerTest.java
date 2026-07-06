package com.SCampus.curso_seccion.controller;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.service.CursoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para CursoController")
public class CursoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CursoService cursoService;

    @InjectMocks
    private CursoController cursoController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private CursoResponseDTO cursoResponseMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cursoController).build();

        cursoResponseMock = new CursoResponseDTO();
        cursoResponseMock.setId(12L);
        cursoResponseMock.setNombre("Programación");
        cursoResponseMock.setFechaCreacion("14/06/26");
    }

    @Test
    @DisplayName("GET /api/cursos - Debe retornar 200 con la lista de cursos")
    void obtenerTodos_DebeRetornarStatus200YLista() throws Exception {
        when(cursoService.obtenerTodos()).thenReturn(List.of(cursoResponseMock));

        mockMvc.perform(get("/api/cursos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(12))
                .andExpect(jsonPath("$[0].fechaCreacion").value("14/06/26"));

        verify(cursoService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("POST /api/cursos/guardar - Debe retornar 201 si los datos son correctos")
    void guardar_DebeRetornarStatus201YCursoResponse() throws Exception {
        CursoRequestDTO requestDTO = new CursoRequestDTO();
        requestDTO.setNombre("Programación");
        requestDTO.setFechaCreacion("14/06/26");

        when(cursoService.guardarCurso(any(CursoRequestDTO.class))).thenReturn(cursoResponseMock);

        mockMvc.perform(post("/api/cursos/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.fechaCreacion").value("14/06/26"));

        verify(cursoService, times(1)).guardarCurso(any(CursoRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/cursos/{id} - Debe retornar 200 si el ID existe en la BD")
    void obtenerPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        when(cursoService.obtenerPorId(12L)).thenReturn(Optional.of(cursoResponseMock));

        mockMvc.perform(get("/api/cursos/12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.fechaCreacion").value("14/06/26"));

        verify(cursoService, times(1)).obtenerPorId(12L);
    }

    @Test
    @DisplayName("GET /api/cursos/{id} - Debe retornar 404 si el ID no existe")
    void obtenerPorId_DebeRetornarStatus404_CuandoIdNoExiste() throws Exception {
        when(cursoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cursos/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cursoService, times(1)).obtenerPorId(99L);
    }

    @Test
    @DisplayName("POST /api/cursos/guardar - Debe retornar 400 Bad Request si body es invalido o vacio")
    void guardar_DebeRetornar400_CuandoBodyVacio() throws Exception {
        mockMvc.perform(post("/api/cursos/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}