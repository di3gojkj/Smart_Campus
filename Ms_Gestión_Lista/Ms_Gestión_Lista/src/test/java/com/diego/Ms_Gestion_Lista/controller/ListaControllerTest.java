package com.diego.Ms_Gestion_Lista.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.diego.Ms_Gestion_Lista.dto.ListaRequestDTO;
import com.diego.Ms_Gestion_Lista.dto.ListaResponseDTO;
import com.diego.Ms_Gestion_Lista.service.AcademicoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ListaController.class, properties = {"spring.security.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Tests del ListaController con MockMvc")
public class ListaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcademicoService academicoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/listas retorna 200 OK")
    void listar_debeRetornar200() throws Exception {
        ListaResponseDTO dto = new ListaResponseDTO(1L, 10L, 5L, LocalDateTime.now());
        when(academicoService.obtenerTodasLasListas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/listas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idLista").value(1))
                .andExpect(jsonPath("$[0].idUser").value(10));
    }

    @Test
    @DisplayName("POST /api/listas retorna 201 Created")
    void insertar_debeRetornar201() throws Exception {
        ListaRequestDTO request = new ListaRequestDTO();
        request.setIdUser(10L);
        request.setIdCurso(5L);

        ListaResponseDTO response = new ListaResponseDTO(1L, 10L, 5L, LocalDateTime.now());
        when(academicoService.crearLista(any(ListaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/listas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUser").value(10));
    }
}