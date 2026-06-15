package MS.tipo_asistencia.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // CORREGIDO: Ruta adaptada a tu pom.xml institucional
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

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.service.AsistenciaService;

@WebMvcTest(AsistenciaController.class)
@DisplayName("Tests del AsistenciaController con MockMvc")
public class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AsistenciaService asistenciaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/asistencia debe retornar un JSON con la lista de asistencias y el codigo 200")
    void obtenerTodas_debeRetornar200ConListaDeAsistencias() throws Exception {
        TipoResponseDTO tipoDto = new TipoResponseDTO(1L, "PRESENTE");
        AsistenciaResponseDTO responseDto = new AsistenciaResponseDTO(15L, "2026-06-14", tipoDto);
        
        when(asistenciaService.obtenerTodas()).thenReturn(List.of(responseDto));

        // CORREGIDO: Se agregó $[0] a las aserciones porque el endpoint devuelve una lista (Array JSON)
        mockMvc.perform(get("/api/asistencia")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idAsistencia").value(15))
                .andExpect(jsonPath("$[0].fecha").value("2026-06-14"))
                .andExpect(jsonPath("$[0].tipo.nombre").value("PRESENTE"));
    }

    @Test
    @DisplayName("GET /api/asistencia/{id} debe retornar la asistencia correspondiente y el codigo 200")
    void obtenerPorId_debeRetornar200ConAsistencia() throws Exception {
        TipoResponseDTO tipoDto = new TipoResponseDTO(1L, "PRESENTE");
        AsistenciaResponseDTO responseDto = new AsistenciaResponseDTO(15L, "2026-06-14", tipoDto);
        
        when(asistenciaService.obtenerPorId(15L)).thenReturn(responseDto);

        // Aquí está correcto sin $[0] porque el endpoint retorna un único objeto directo
        mockMvc.perform(get("/api/asistencia/15")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idAsistencia").value(15))
                .andExpect(jsonPath("$.fecha").value("2026-06-14"));
    }

    @Test
    @DisplayName("POST /api/asistencia debe retornar 201 con datos validos")
    void crear_debeRetornar201_cuandoDatosValidos() throws Exception {
        AsistenciaRequestDTO request = new AsistenciaRequestDTO("2026-06-14", 1L);
        TipoResponseDTO tipoDto = new TipoResponseDTO(1L, "PRESENTE");
        AsistenciaResponseDTO response = new AsistenciaResponseDTO(15L, "2026-06-14", tipoDto);
        
        when(asistenciaService.crear(any(AsistenciaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/asistencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAsistencia").value(15))
                .andExpect(jsonPath("$.fecha").value("2026-06-14"));
    }

    @Test
    @DisplayName("PUT /api/asistencia/{id} debe retornar 200 al actualizar con datos validos")
    void actualizar_debeRetornar200_cuandoDatosValidos() throws Exception {
        AsistenciaRequestDTO request = new AsistenciaRequestDTO("2026-06-15", 2L);
        TipoResponseDTO tipoDto = new TipoResponseDTO(2L, "AUSENTE");
        AsistenciaResponseDTO response = new AsistenciaResponseDTO(15L, "2026-06-15", tipoDto);
        
        when(asistenciaService.actualizar(eq(15L), any(AsistenciaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/asistencia/15")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fecha").value("2026-06-15"))
                .andExpect(jsonPath("$.tipo.nombre").value("AUSENTE"));
    }

    @Test
    @DisplayName("DELETE /api/asistencia/{id} debe retornar 24 al eliminar un registro")
    void eliminar_debeRetornar204_cuandoSeElimina() throws Exception {
        mockMvc.perform(delete("/api/asistencia/15")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
