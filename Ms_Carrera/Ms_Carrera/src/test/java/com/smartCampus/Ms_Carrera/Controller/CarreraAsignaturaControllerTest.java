package com.smartCampus.Ms_Carrera.Controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("GET /api/carreras-asignaturas debe retornar 200 y la lista")
    void listar_debeRetornar200ConLista() throws Exception {
        CarreraAsignaturaResponseDTO dto = new CarreraAsignaturaResponseDTO();
        dto.setIdCarrera(1L);
        dto.setIdAsignatura(3L);


        
        // Act: Configuramos el mock para el método que existe en TU servicio
        // Usamos any() porque la petición GET no está enviando el ID 1L explícitamente
        when(carreraAsignaturaService.listarTodas(any())).thenReturn(List.of(dto));

        // Assert: Validamos la petición
        mockMvc.perform(get("/api/carreras-asignaturas")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].idAsignatura").value(3)) // Quitamos la L (Jackson lee Integers)
            .andExpect(jsonPath("$[0].idCarrera").value(1));
    }

    @Test
    @DisplayName("POST api/carrera-asignaturas debe retorna 201 con datos validos")
    void crear_debeRetornar201() throws Exception {
        CarreraAsignaturaRequestDTO request = new CarreraAsignaturaRequestDTO(1L,1L,1L);
        // ERROR AQUÍ: El servicio de relación debe devolver el DTO de la relación, no de la Carrera
        CarreraAsignaturaResponseDTO response = new CarreraAsignaturaResponseDTO();
        response.setIdCarrera(1L);
        response.setIdAsignatura(1L);
        response.setIdSemestre(1L);
        
        when(carreraAsignaturaService.crear(any(CarreraAsignaturaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/carreras-asignaturas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCarrera").value(1));
    }
}
