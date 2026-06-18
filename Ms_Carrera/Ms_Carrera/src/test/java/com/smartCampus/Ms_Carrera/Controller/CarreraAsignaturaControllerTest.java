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

import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaRequestDTO;
import com.smartCampus.Ms_Carrera.DTO.CarreraAsignaturaResponseDTO;
import com.smartCampus.Ms_Carrera.Service.CarreraAsignaturaService;


@WebMvcTest(CarreraAsignaturaController.class)
@DisplayName("Tests del CarreraAsignaturaController con MockMvc")
public class CarreraAsignaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarreraAsignaturaService carreraAsignaturaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/carrera-asignaturas/carrera/{id} debe retornar 200 y la lista")
    void listar_debeRetornar200ConLista() throws Exception {
        CarreraAsignaturaResponseDTO dto = new CarreraAsignaturaResponseDTO();
        dto.setIdCarrera(1L);
        dto.setIdAsignatura(3L);
        when(carreraAsignaturaService.listarTodas(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/carrera-asignaturas/carrera/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].idAsignatura").value(3))
            .andExpect(jsonPath("$[0].idCarrera").value(1));
    }

    @Test
    @DisplayName("POST /api/carrera-asignaturas debe retornar 201 con datos validos")
    void crear_debeRetornar201() throws Exception {
        CarreraAsignaturaRequestDTO req = new CarreraAsignaturaRequestDTO(1L, 1L, 1L);
        CarreraAsignaturaResponseDTO res = new CarreraAsignaturaResponseDTO();
        res.setIdCarrera(1L);
        res.setIdAsignatura(1L);
        res.setIdSemestre(1L);
        when(carreraAsignaturaService.crear(any(CarreraAsignaturaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/carrera-asignaturas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idCarrera").value(1));
    }

    @Test
    @DisplayName("PUT /api/carrera-asignaturas/{id} debe retornar 200 al actualizar")
    void actualizar_debeRetornar200() throws Exception {
        CarreraAsignaturaRequestDTO req = new CarreraAsignaturaRequestDTO(1L, 2L, 2L);
        CarreraAsignaturaResponseDTO res = new CarreraAsignaturaResponseDTO();
        res.setIdCarrera(1L);
        res.setIdAsignatura(2L);
        res.setIdSemestre(2L);
        when(carreraAsignaturaService.actualizar(eq(1L), any(CarreraAsignaturaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/carrera-asignaturas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idAsignatura").value(2));
    }

    @Test
    @DisplayName("DELETE /api/carrera-asignaturas/{id} debe retornar 204 No Content")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(carreraAsignaturaService).eliminar(1L);

        mockMvc.perform(delete("/api/carrera-asignaturas/1"))
            .andExpect(status().isNoContent());
    }
}
