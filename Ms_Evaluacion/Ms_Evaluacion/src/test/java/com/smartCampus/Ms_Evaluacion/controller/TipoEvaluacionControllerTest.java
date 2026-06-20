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
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionRequestDTO;
import com.smartCampus.Ms_Evaluacion.DTO.TipoEvaluacionResponseDTO;
import com.smartCampus.Ms_Evaluacion.service.TipoEvaluacionService;

@WebMvcTest(TipoEvaluacionController.class)
@DisplayName("Tests del TipoEvaluacionController con MockMvc")
public class TipoEvaluacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoEvaluacionService tipoEvaluacionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/tipo-evaluacion debe retornar 200 y JSON con lista")
    void listarTodos_debeRetornar200() throws Exception {
        TipoEvaluacionResponseDTO dto = new TipoEvaluacionResponseDTO();
        dto.setIdTipoEval(1L);
        dto.setNombreTipo("Certamen");
        when(tipoEvaluacionService.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tipo-evaluacion").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].nombreTipo").value("Certamen"));
    }

    @Test
    @DisplayName("GET /api/tipo-evaluacion/{id} debe retornar 200 con el tipo encontrado")
    void buscarPorId_debeRetornar200() throws Exception {
        TipoEvaluacionResponseDTO dto = new TipoEvaluacionResponseDTO();
        dto.setIdTipoEval(1L);
        dto.setNombreTipo("Certamen");
        when(tipoEvaluacionService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/tipo-evaluacion/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idTipoEval").value(1L))
            .andExpect(jsonPath("$.nombreTipo").value("Certamen"));
    }

    @Test
    @DisplayName("POST /api/tipo-evaluacion debe retornar 201 al crear")
    void crear_debeRetornar201() throws Exception {
        TipoEvaluacionRequestDTO req = new TipoEvaluacionRequestDTO();
        req.setNombreTipo("Certamen");

        TipoEvaluacionResponseDTO res = new TipoEvaluacionResponseDTO();
        res.setIdTipoEval(1L);
        res.setNombreTipo("Certamen");
        when(tipoEvaluacionService.crear(any(TipoEvaluacionRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/tipo-evaluacion")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idTipoEval").value(1L));
    }

    @Test
    @DisplayName("PUT /api/tipo-evaluacion/{id} debe retornar 200 al actualizar")
    void actualizar_debeRetornar200() throws Exception {
        TipoEvaluacionRequestDTO req = new TipoEvaluacionRequestDTO();
        req.setNombreTipo("Certamen Modificado");

        TipoEvaluacionResponseDTO res = new TipoEvaluacionResponseDTO();
        res.setIdTipoEval(1L);
        res.setNombreTipo("Certamen Modificado");
        when(tipoEvaluacionService.actualizar(eq(1L), any(TipoEvaluacionRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/tipo-evaluacion/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombreTipo").value("Certamen Modificado"));
    }

    @Test
    @DisplayName("DELETE /api/tipo-evaluacion/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(tipoEvaluacionService).eliminar(1L);

        mockMvc.perform(delete("/api/tipo-evaluacion/1"))
            .andExpect(status().isNoContent());
    }

}
