package com.smartcampus.msAsignatura.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.smartcampus.msAsignatura.DTO.AsignaturaRequestDTO;
import com.smartcampus.msAsignatura.DTO.AsignaturaResponseDTO;
import com.smartcampus.msAsignatura.service.AsignaturaService;



@WebMvcTest(AsignaturaController.class)
@DisplayName("Tests del Controller con MockMvc")
public class AsignaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsignaturaService asignaturaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/asignaturas debe retornar 200 y JSON con lista")
    void listarTodas_debeRetornar200() throws Exception {
        AsignaturaResponseDTO dto = new AsignaturaResponseDTO(); dto.setIdAsignatura(1L); dto.setNombre("Desarrollo en Fullstack");
        when(asignaturaService.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/asignaturas").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Desarrollo en Fullstack"));
    }

    @Test
    @DisplayName("GET /api/asignaturas/{id} debe retornar 200 con la asignatura encontrada")
    void buscarPorId_debeRetornar200() throws Exception {
        AsignaturaResponseDTO dto = new AsignaturaResponseDTO();
        dto.setIdAsignatura(1L);
        dto.setNombre("Desarrollo en Fullstack");
        when(asignaturaService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/asignaturas/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idAsignatura").value(1L))
            .andExpect(jsonPath("$.nombre").value("Desarrollo en Fullstack"));
    }

    @Test
    @DisplayName("POST /api/asignaturas debe retornar 201 al crear")
    void crear_debeRetornar201() throws Exception {
        AsignaturaRequestDTO req = new AsignaturaRequestDTO(); 
        req.setNombre("Nueva Asignatura");
        req.setSigla("INF-230");      
        req.setIdEstado(1L); 
        AsignaturaResponseDTO res = new AsignaturaResponseDTO(); res.setIdAsignatura(1L); res.setNombre("Nueva Asignatura");
        when(asignaturaService.crear(any(AsignaturaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/asignaturas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idAsignatura").value(1L));
    }

    @Test
    @DisplayName("PUT /api/asignaturas/{id} debe retornar 200 al actualizar")
    void actualizar_debeRetornar200() throws Exception {
        AsignaturaRequestDTO req = new AsignaturaRequestDTO(); 
        req.setNombre("Modificada");
        req.setSigla("INF-999");      
        req.setIdEstado(1L); 
        AsignaturaResponseDTO res = new AsignaturaResponseDTO(); res.setNombre("Modificada");
        when(asignaturaService.actualizar(eq(1L), any(AsignaturaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/asignaturas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Modificada"));
    }

    @Test
    @DisplayName("DELETE /api/asignaturas/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(asignaturaService).eliminar(1L);

        mockMvc.perform(delete("/api/asignaturas/1"))
            .andExpect(status().isNoContent());
    }
    

}
