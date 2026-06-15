package com.cur_eva.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // Sincronizado con tu pom.xml institucional
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

import com.cur_eva.dto.CursoEvaluacionRequestDTO;
import com.cur_eva.dto.CursoEvaluacionResponseDTO;
import com.cur_eva.service.CursoEvaluacionService;

@WebMvcTest(CursoEvaluacionController.class)
@DisplayName("Tests del CursoEvaluacionController con MockMvc")
public class CursoEvaluacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CursoEvaluacionService cursoEvaluacionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/estados debe retornar un JSON con la lista de evaluaciones y el codigo 200")
    void obtenerTodos_debeRetornar200ConListaDeEvaluaciones() throws Exception {
        // Arrange: Creamos un DTO de respuesta simulado
        CursoEvaluacionResponseDTO responseDto = new CursoEvaluacionResponseDTO(
            1L, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20"
        );
        
        when(cursoEvaluacionService.obtenerTodos()).thenReturn(List.of(responseDto));

        // Act & Assert
        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // Imprime la traza por consola para seguimiento
                .andExpect(status().isOk()) // HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"))
                .andExpect(jsonPath("$[0].fCreacion").value("2026-06-15"));

        verify(cursoEvaluacionService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/estados/{id} debe retornar la evaluación correspondiente y el codigo 200")
    void obtenerPorId_debeRetornar200ConEvaluacion() throws Exception {
        // Arrange
        CursoEvaluacionResponseDTO responseDto = new CursoEvaluacionResponseDTO(
            1L, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20"
        );
        
        when(cursoEvaluacionService.obtenerPorId(1L)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));

        verify(cursoEvaluacionService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("POST /api/estados debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception {
        // Arrange: Objetos simulados para la petición (Request) y la salida (Response)
        CursoEvaluacionRequestDTO request = new CursoEvaluacionRequestDTO(
            "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20"
        );
        CursoEvaluacionResponseDTO response = new CursoEvaluacionResponseDTO(
            1L, "ACTIVO", "2026-06-15", "2026-07-20", "2026-06-20"
        );
        
        when(cursoEvaluacionService.guardar(any(CursoEvaluacionRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Serializa objeto Java a JSON String
                .andDo(print())
                .andExpect(status().isCreated()) // HTTP 201 Created
                .andExpect(jsonPath("$.idCursoEvaluacion").value(1))
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));

        verify(cursoEvaluacionService, times(1)).guardar(any(CursoEvaluacionRequestDTO.class));
    }
}

