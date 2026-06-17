package com.diego.Ms_Gestion_Estado.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.diego.Ms_Gestion_Estado.dto.EstadoRequestDTO;
import com.diego.Ms_Gestion_Estado.dto.EstadoResponseDTO;
import com.diego.Ms_Gestion_Estado.service.EstadoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EstadoController.class, properties = {"spring.security.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests del EstadoController con MockMvc")
public class EstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoService estadoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/estados retorna 200 OK")
    void obtenerTodos_debeRetornar200() throws Exception {
        when(estadoService.obtenerTodos()).thenReturn(List.of(new EstadoResponseDTO(1L, "ACTIVO")));

        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"));
    }

    @Test
    @DisplayName("POST /api/estados retorna 201 Created")
    void crear_debeRetornar201() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO("INACTIVO");
        EstadoResponseDTO response = new EstadoResponseDTO(2L, "INACTIVO");

        when(estadoService.guardar(any(EstadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstado").value(2))
                .andExpect(jsonPath("$.nombre").value("INACTIVO"));
    }
}