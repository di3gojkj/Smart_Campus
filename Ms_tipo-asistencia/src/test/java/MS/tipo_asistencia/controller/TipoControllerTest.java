package MS.tipo_asistencia.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.service.TipoService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para TipoController")
public class TipoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TipoService tipoService;

    @InjectMocks
    private TipoController tipoController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private TipoResponseDTO tipoResponseMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tipoController).build();

        tipoResponseMock = new TipoResponseDTO();
        tipoResponseMock.setIdTipo(1L);
        tipoResponseMock.setNombre("JUSTIFICADO");
    }

    @Test
    @DisplayName("GET /api/tipo - Debe retornar status 200 y el listado de catálogo completo")
    void obtenerTodos_DebeRetornarStatus200YListaDeTipos() throws Exception {
        when(tipoService.obtenerTodas()).thenReturn(List.of(tipoResponseMock));

        mockMvc.perform(get("/api/tipo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idTipo").value(1))
                .andExpect(jsonPath("$[0].nombre").value("JUSTIFICADO"));

        verify(tipoService, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("GET /api/tipo/{id} - Debe retornar status 200 si la clasificación existe en el catálogo")
    void obtenerPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        when(tipoService.obtenerPorId(1L)).thenReturn(tipoResponseMock);

        mockMvc.perform(get("/api/tipo/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipo").value(1))
                .andExpect(jsonPath("$.nombre").value("JUSTIFICADO"));

        verify(tipoService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/tipo/{id} - Debe retornar status 404 si la clasificación no existe")
    void obtenerPorId_DebeRetornarStatus404_CuandoIdNoExiste() throws Exception {
        when(tipoService.obtenerPorId(999L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/tipo/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(tipoService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("POST /api/tipo - Debe retornar status 201 al registrar un nuevo tipo válido")
    void crear_DebeRetornarStatus201YTipoCreado() throws Exception {
        TipoRequestDTO inputDto = new TipoRequestDTO();
        inputDto.setNombre("JUSTIFICADO");
        inputDto.setTipoId(1L);

        when(tipoService.crear(any(TipoRequestDTO.class))).thenReturn(tipoResponseMock);

        mockMvc.perform(post("/api/tipo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTipo").value(1))
                .andExpect(jsonPath("$.nombre").value("JUSTIFICADO"));

        verify(tipoService, times(1)).crear(any(TipoRequestDTO.class));
    }


}
