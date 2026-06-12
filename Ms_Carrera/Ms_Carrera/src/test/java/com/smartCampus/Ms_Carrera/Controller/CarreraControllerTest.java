package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("GET api/carreras debe retornar un JSON con la lista de productos y el codigo 200")
    void listar_debeRetornar200() throws Exception {
        CarreraResponseDTO dto = new CarreraResponseDTO(1L,"Ingenieria en informatica", "INF-001", 1L);
        when(carreraService.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/carreras")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Ingenieria en informatica"));
    }

    @Test
    @DisplayName("POST api/carreras debe retorna 201 con datos validos")
    void crear_debeRetornar201() throws Exception {
        CarreraRequestDTO request = new CarreraRequestDTO("Ingenieria", "INF-001", 1L);
        CarreraResponseDTO response = new CarreraResponseDTO(1L,"Ingenieria", "INF-001", 1L);
        
        when(carreraService.crear(any(CarreraRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/carreras").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ingenieria"));
    }

}
