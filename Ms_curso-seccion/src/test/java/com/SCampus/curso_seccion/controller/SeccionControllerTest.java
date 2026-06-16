package com.SCampus.curso_seccion.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // Importación institucional
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.service.SeccionService;

@WebMvcTest(SeccionController.class)
@DisplayName("Tests del SeccionController con MockMvc")
public class SeccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SeccionService seccionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/seccion debe retornar un JSON con la lista de secciones y el codigo 200")
    void obtenerSecciones_debeRetornar200ConLista() throws Exception {
        Seccion seccionMock = new Seccion(5L, "Sección A", 12L);
        when(seccionService.obtenerTodas()).thenReturn(List.of(seccionMock));

        mockMvc.perform(get("/api/seccion")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Sección A"))
                .andExpect(jsonPath("$.idCurso").value(12));

        verify(seccionService, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("GET /api/seccion/{id} debe retornar la seccion cuando existe y el codigo 200")
    void obtenerSeccionPorId_debeRetornar200_cuandoExiste() throws Exception {
        Seccion seccionMock = new Seccion(5L, "Sección A", 12L);
        when(seccionService.obtenerPorId(5L)).thenReturn(Optional.of(seccionMock));

        mockMvc.perform(get("/api/seccion/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Sección A"));

        verify(seccionService, times(1)).obtenerPorId(5L);
    }

    @Test
    @DisplayName("GET /api/seccion/{id} debe retornar 404 cuando la seccion no existe")
    void obtenerSeccionPorId_debeRetornar404_cuandoNoExiste() throws Exception {
        when(seccionService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seccion/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(seccionService, times(1)).obtenerPorId(99L);
    }

    @Test
    @DisplayName("POST /api/seccion debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception {
        Seccion request = new Seccion(null, "Sección A", 12L);
        Seccion response = new Seccion(5L, "Sección A", 12L);
        
        when(seccionService.guardar(any(Seccion.class))).thenReturn(response);

        mockMvc.perform(post("/api/seccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Sección A"));

        verify(seccionService, times(1)).guardar(any(Seccion.class));
    }

    @Test
    @DisplayName("PUT /api/seccion/{id} debe retornar 200 al actualizar con datos validos")
    void actualizar_debeRetornar200_cuandoSeActualiza() throws Exception {
        Seccion request = new Seccion(null, "Sección A Modificada", 12L);
        Seccion existente = new Seccion(5L, "Sección A", 12L);
        Seccion guardada = new Seccion(5L, "Sección A Modificada", 12L);

        when(seccionService.obtenerPorId(5L)).thenReturn(Optional.of(existente));
        when(seccionService.guardar(any(Seccion.class))).thenReturn(guardada);

        mockMvc.perform(put("/api/seccion/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nombre").value("Sección A Modificada"));

        verify(seccionService, times(1)).obtenerPorId(5L);
        verify(seccionService, times(1)).guardar(any(Seccion.class));
    }

    @Test
    @DisplayName("DELETE /api/seccion/{id} debe retornar 24 al eliminar un registro existente")
    void eliminar_debeRetornar204_cuandoSeElimina() throws Exception {
        Seccion existente = new Seccion(5L, "Sección A", 12L);
        when(seccionService.obtenerPorId(5L)).thenReturn(Optional.of(existente));

        mockMvc.perform(delete("/api/seccion/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(seccionService, times(1)).obtenerPorId(5L);
        verify(seccionService, times(1)).eliminar(5L);
    }
}
