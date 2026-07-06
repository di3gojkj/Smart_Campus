package com.diego.Ms_Gestion_Lista.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import com.diego.Ms_Gestion_Lista.dto.CalificacionRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.CalificacionResponseDTO;
import com.diego.Ms_Gestion_Lista.service.AcademicoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CalificacionController.class, properties = {"spring.security.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests del CalificacionController con MockMvc")
public class CalificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcademicoService academicoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/calificaciones/lista/{idLista} retorna 200 OK con datos")
    void listarPorLista_debeRetornar200() throws Exception {
        CalificacionResponseDTO dto = new CalificacionResponseDTO(1L, new BigDecimal("6.5"), 1L, 2L);
        when(academicoService.obtenerCalificacionesPorLista(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/calificaciones/lista/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nota").value(6.5));
    }

    @Test
    @DisplayName("GET /api/calificaciones/lista/{idLista} retorna 200 OK con vacio")
    void listarPorLista_vacia() throws Exception {
        when(academicoService.obtenerCalificacionesPorLista(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/calificaciones/lista/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /api/calificaciones evalúa correctamente")
    void evaluar_debeRetornar201() throws Exception {
        CalificacionRequestDTO request = new CalificacionRequestDTO();
        request.setNota(new BigDecimal("7.0"));
        request.setIdLista(1L);
        request.setIdCurEva(2L);

        CalificacionResponseDTO response = new CalificacionResponseDTO(1L, new BigDecimal("7.0"), 1L, 2L);
        when(academicoService.registrarCalificacion(any(CalificacionRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/calificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nota").value(7.0));
    }

    @Test
    @DisplayName("POST /api/calificaciones falla con 400 Bad Request por Body vacio")
    void evaluar_badRequest() throws Exception {
        mockMvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }
}