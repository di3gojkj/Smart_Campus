package com.diego.Ms_Gestion_Estado.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
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
import com.diego.Ms_Gestion_Estado.exception.EstadoNotFoundException;
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
    @DisplayName("GET /api/estados retorna 200 OK y la lista de estados")
    void obtenerTodos_debeRetornar200() throws Exception {
        when(estadoService.obtenerTodos()).thenReturn(List.of(new EstadoResponseDTO(1L, "ACTIVO")));

        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("ACTIVO"));
    }

    @Test
    @DisplayName("GET /api/estados retorna 200 OK y lista vacía cuando no hay datos")
    void obtenerTodos_debeRetornarListaVacia() throws Exception {
        when(estadoService.obtenerTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/estados/{id} retorna 200 OK cuando el estado existe")
    void obtenerPorId_debeRetornar200_cuandoExiste() throws Exception {
        when(estadoService.obtenerPorId(1L)).thenReturn(new EstadoResponseDTO(1L, "ACTIVO"));

        mockMvc.perform(get("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ACTIVO"));
    }

    @Test
    @DisplayName("GET /api/estados/{id} lanza excepción cuando el estado no existe")
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() throws Exception {
        when(estadoService.obtenerPorId(99L)).thenThrow(new EstadoNotFoundException(99L));

        mockMvc.perform(get("/api/estados/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(EstadoNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("POST /api/estados retorna 201 Created con datos válidos")
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

    @Test
    @DisplayName("POST /api/estados retorna 400 Bad Request si faltan datos")
    void crear_debeRetornar400_cuandoFaltanDatos() throws Exception {
        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/estados maneja la excepción cuando el servicio falla (ej. estado duplicado)")
    void crear_lanzaExcepcion_cuandoServicioFalla() throws Exception {
        EstadoRequestDTO request = new EstadoRequestDTO("DUPLICADO");
        
        when(estadoService.guardar(any(EstadoRequestDTO.class)))
            .thenThrow(new RuntimeException("Ya existe un estado con el nombre"));

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertTrue(result.getResolvedException().getMessage().contains("Ya existe un estado")));
    }
}