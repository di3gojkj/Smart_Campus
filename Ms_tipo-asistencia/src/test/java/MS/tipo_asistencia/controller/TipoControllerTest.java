package MS.tipo_asistencia.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // Ajustado a tus librerías
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

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.service.TipoService;

@WebMvcTest(TipoController.class)
@DisplayName("Tests del TipoController con MockMvc")
public class TipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoService tipoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/tipo debe retornar un JSON con la lista de tipos de asistencia y el codigo 200")
    void obtenerTodas_debeRetornar200ConListaDeTipos() throws Exception {
        TipoResponseDTO dto = new TipoResponseDTO(1L, "PRESENTE");
        when(tipoService.obtenerTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tipo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idTipo").value(1))
                .andExpect(jsonPath("$[0].nombre").value("PRESENTE"));
    }

    @Test
    @DisplayName("GET /api/tipo/{id} debe retornar el tipo de asistencia y el codigo 200")
    void obtenerPorId_debeRetornar200ConTipo() throws Exception {
        TipoResponseDTO dto = new TipoResponseDTO(1L, "PRESENTE");
        when(tipoService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/tipo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTipo").value(1))
                .andExpect(jsonPath("$.nombre").value("PRESENTE"));
    }

    @Test
    @DisplayName("POST /api/tipo debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception {
        // CORREGIDO: Se elimina el parámetro manual '2L' para adaptarse al nuevo constructor de 1 solo parámetro
        TipoRequestDTO request = new TipoRequestDTO("AUSENTE", null);
        TipoResponseDTO response = new TipoResponseDTO(2L, "AUSENTE");
        
        when(tipoService.crear(any(TipoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTipo").value(2))
                .andExpect(jsonPath("$.nombre").value("AUSENTE"));
    }

    @Test
    @DisplayName("PUT /api/tipo/{id} debe retornar 200 al actualizar con datos validos")
    void actualizar_debeRetornar200_cuandoDatosValidos() throws Exception {
        // CORREGIDO: Se elimina el parámetro manual '3L' del constructor del Request
        TipoRequestDTO request = new TipoRequestDTO("JUSTIFICADO", null);
        TipoResponseDTO response = new TipoResponseDTO(3L, "JUSTIFICADO");
        
        when(tipoService.actualizar(eq(3L), any(TipoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/tipo/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipo").value(3))
                .andExpect(jsonPath("$.nombre").value("JUSTIFICADO"));
    }

    @Test
    @DisplayName("DELETE /api/tipo/{id} debe retornar 24 al eliminar una clasificacion")
    void eliminar_debeRetornar204_cuandoSeElimina() throws Exception {
        mockMvc.perform(delete("/api/tipo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

