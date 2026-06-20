package com.smartCampus.Ms_Evaluacion.controller;

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

import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.EvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.EvaluacionService;



@WebMvcTest(EvaluacionController.class)
@DisplayName("Tests del EvaluacionController con MockMvc")
public class EvaluacionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvaluacionService evaluacionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/evaluacion debe retornar 200 y JSON con lista")
    void listarTodas_debeRetornar200() throws Exception {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setIdEvaluacion(1L);
        dto.setNombre("Certamen 1");
        dto.setPorcentaje(25.0);
        dto.setIdTipoEval(2L);
        when(evaluacionService.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/evaluacion").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombre").value("Certamen 1"));
    }

    @Test
    @DisplayName("GET /api/evaluacion/{id} debe retornar 200 con la evaluacion encontrada")
    void buscarPorId_debeRetornar200() throws Exception {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setIdEvaluacion(1L);
        dto.setNombre("Certamen 1");
        when(evaluacionService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/evaluacion/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idEvaluacion").value(1L))
            .andExpect(jsonPath("$.nombre").value("Certamen 1"));
    }

    @Test
    @DisplayName("POST /api/evaluacion debe retornar 201 al crear")
    void crear_debeRetornar201() throws Exception {
        EvaluacionRequestDTO req = new EvaluacionRequestDTO();
        req.setNombre("Certamen 1");
        req.setPorcentaje(25.0);
        req.setIdTipoEval(2L);

        EvaluacionResponseDTO res = new EvaluacionResponseDTO();
        res.setIdEvaluacion(1L);
        res.setNombre("Certamen 1");
        when(evaluacionService.crear(any(EvaluacionRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/evaluacion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idEvaluacion").value(1L));
    }

    @Test
    @DisplayName("PUT /api/evaluacion/{id} debe retornar 200 al actualizar")
    void actualizar_debeRetornar200() throws Exception {
        EvaluacionRequestDTO req = new EvaluacionRequestDTO();
        req.setNombre("Certamen Modificado");
        req.setPorcentaje(30.0);
        req.setIdTipoEval(2L);

        EvaluacionResponseDTO res = new EvaluacionResponseDTO();
        res.setIdEvaluacion(1L);
        res.setNombre("Certamen Modificado");
        when(evaluacionService.actualizar(eq(1L), any(EvaluacionRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/evaluacion/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Certamen Modificado"));
    }

    @Test
    @DisplayName("DELETE /api/evaluacion/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(evaluacionService).eliminar(1L);

        mockMvc.perform(delete("/api/evaluacion/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/evaluacion/tipo/{id} debe retornar 200 y filtrar evaluaciones por su tipo")
    void listarPorTipo_debeRetornar200() throws Exception {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setIdEvaluacion(1L);
        dto.setIdTipoEval(2L);
        when(evaluacionService.buscarPorTipo(2L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/evaluacion/tipo/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idTipoEval").value(2L));
    }
}
