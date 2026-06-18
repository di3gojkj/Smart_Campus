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
import com.smartcampus.msAsignatura.DTO.SemestreRequestDTO;
import com.smartcampus.msAsignatura.DTO.SemestreResponseDTO;
import com.smartcampus.msAsignatura.service.SemestreService;

@WebMvcTest(SemestreController.class)
@DisplayName("Tests del SemestreController con MockMvc")
public class SemestreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private SemestreService semestreService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/semestres debe retornar 200 y la lista cronologica")
    void listarTodos_debeRetornar200() throws Exception {
        SemestreResponseDTO dto = new SemestreResponseDTO(); dto.setIdSemestre(1L); dto.setNombre("2026-1");
        when(semestreService.listarTodosCronologicos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/semestres").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("2026-1"));
    }

    @Test
    @DisplayName("GET /api/semestres/{id} debe retornar 200 con el DTO encontrado")
    void buscarPorId_debeRetornar200() throws Exception {
        SemestreResponseDTO dto = new SemestreResponseDTO(); dto.setIdSemestre(1L); dto.setNombre("2026-2");
        when(semestreService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/semestres/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("2026-2"));
    }

    @Test
    @DisplayName("POST /api/semestres debe retornar 201 al registrar")
    void crear_debeRetornar201() throws Exception {
        SemestreRequestDTO req = new SemestreRequestDTO(); 
        req.setNombre("2027-1");
        req.setIdEstado(1L);
        SemestreResponseDTO res = new SemestreResponseDTO(); res.setIdSemestre(1L); res.setNombre("2027-1");
        when(semestreService.crear(any(SemestreRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/semestres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idSemestre").value(1L));
    }

    @Test
    @DisplayName("PUT /api/semestres/{id} debe retornar 200 al editar")
    void actualizar_debeRetornar200() throws Exception {
        SemestreRequestDTO req = new SemestreRequestDTO(); 
        req.setNombre("2026-1 Modificado");
        req.setIdEstado(1L);
        SemestreResponseDTO res = new SemestreResponseDTO(); res.setNombre("2026-1 Modificado");
        when(semestreService.actualizar(eq(1L), any(SemestreRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/semestres/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("2026-1 Modificado"));
    }

    @Test
    @DisplayName("DELETE /api/semestres/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(semestreService).eliminar(1L);

        mockMvc.perform(delete("/api/semestres/1"))
            .andExpect(status().isNoContent());
    }
}
