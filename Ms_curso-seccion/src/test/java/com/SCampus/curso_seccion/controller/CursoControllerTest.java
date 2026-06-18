package com.SCampus.curso_seccion.controller;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.SCampus.curso_seccion.client.CarreraClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // Importación institucional corregida
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SCampus.curso_seccion.dto.CarreraResponseDTO;
import com.SCampus.curso_seccion.dto.CursoRequestDTO;
import com.SCampus.curso_seccion.dto.CursoResponseDTO;
import com.SCampus.curso_seccion.service.CursoService;

@WebMvcTest(CursoController.class)
@DisplayName("Tests del CursoController con MockMvc")
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoService cursoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/cursos debe retornar un JSON con la lista de cursos y el codigo 200")
    void obtenerTodos_debeRetornar200ConListaDeCursos() throws Exception {
        // Arrange
        CursoResponseDTO responseDto = new CursoResponseDTO(12L, "14/06/26");
        when(cursoService.obtenerTodos()).thenReturn(List.of(responseDto));

        // Act & Assert
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
    @DisplayName("POST /api/cursos/guardar debe retornar 201 de manera exitosa con datos válidos")
    void guardar_debeRetornar201_cuandoDatosValidos() throws Exception {
        // Arrange
        CursoRequestDTO request = new CursoRequestDTO("14/06/26");
        CursoResponseDTO response = new CursoResponseDTO(12L, "14/06/26");
        
        when(cursoService.guardarCurso(any(CursoRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/cursos/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.fechaCreacion").value("14/06/26"));

        verify(cursoService, times(1)).guardarCurso(any(CursoRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/cursos/{id} debe retornar 200 y el DTO si el curso existe")
    void obtenerPorId_debeRetornar200_cuandoExiste() throws Exception {
        CursoResponseDTO response = new CursoResponseDTO(12L, "14/06/26");
        when(cursoService.obtenerPorId(12L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/cursos/12")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(12))
            .andExpect(jsonPath("$.fechaCreacion").value("14/06/26"));
    }

    @Test
    @DisplayName("GET /api/cursos/{id} debe retornar 404 si el curso no existe")
    void obtenerPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(cursoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cursos/99")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}
