package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartCampus.Ms_Carrera.DTO.CarreraRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraService;

@WebMvcTest(CarreraController.class)
@DisplayName("Tests del CarreraController con MockMvc")
public class CarreraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarreraService carreraService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/carreras debe retornar 200 y JSON con lista")
    void listarTodas_debeRetornar200() throws Exception {
        CarreraResponseDTO dto = new CarreraResponseDTO(1L, "Ingenieria en Informatica", "INF-001", 1L);
        when(carreraService.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/carreras").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Ingenieria en Informatica"));
    }

    @Test
    @DisplayName("GET /api/carreras/{id} debe retornar 200 con la carrera encontrada")
    void buscarPorId_debeRetornar200() throws Exception {
        CarreraResponseDTO dto = new CarreraResponseDTO(1L, "Ingenieria en Informatica", "INF-001", 1L);
        when(carreraService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/carreras/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idCarrera").value(1L))
            .andExpect(jsonPath("$.nombre").value("Ingenieria en Informatica"));
    }

    @Test
    @DisplayName("POST /api/carreras debe retornar 201 al crear")
    void crear_debeRetornar201() throws Exception {
        CarreraRequestDTO req = new CarreraRequestDTO("Ingenieria en Informatica", "INF-001", 1L);
        CarreraResponseDTO res = new CarreraResponseDTO(1L, "Ingenieria en Informatica", "INF-001", 1L);
        when(carreraService.crear(any(CarreraRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/carreras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idCarrera").value(1L));
    }

    @Test
    @DisplayName("PUT /api/carreras/{id} debe retornar 200 al actualizar")
    void actualizar_debeRetornar200() throws Exception {
        CarreraRequestDTO req = new CarreraRequestDTO("Carrera Modificada", "INF-001", 1L);
        CarreraResponseDTO res = new CarreraResponseDTO(1L, "Carrera Modificada", "INF-001", 1L);
        when(carreraService.actualizar(eq(1L), any(CarreraRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/carreras/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Carrera Modificada"));
    }

    @Test
    @DisplayName("DELETE /api/carreras/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(carreraService).eliminar(1L);

        mockMvc.perform(delete("/api/carreras/1"))
            .andExpect(status().isNoContent());
    }

}
