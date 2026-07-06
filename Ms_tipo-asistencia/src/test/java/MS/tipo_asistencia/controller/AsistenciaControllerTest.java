package MS.tipo_asistencia.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.dto.ListaResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.service.AsistenciaService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para AsistenciaController")
public class AsistenciaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AsistenciaService asistenciaService;

    @InjectMocks
    private AsistenciaController asistenciaController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AsistenciaResponseDTO responseDTOMock;
    private AsistenciaRequestDTO requestDTOMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(asistenciaController).build();

        Tipo tipo = new Tipo(1L, "PRESENTE");

        ListaResponseDTO datosInscripcion = new ListaResponseDTO();
        datosInscripcion.setIdLista(45L);
        datosInscripcion.setIdUser(10L);
        datosInscripcion.setIdCurso(5L);

        responseDTOMock = new AsistenciaResponseDTO();
        responseDTOMock.setIdAsistencia(101L);
        responseDTOMock.setFecha("2026-06-21");
        responseDTOMock.setIdLista(45L);
        responseDTOMock.setTipo(tipo);
        responseDTOMock.setDatosInscripcion(datosInscripcion);

        requestDTOMock = new AsistenciaRequestDTO();
        requestDTOMock.setFecha("2026-06-21");
        requestDTOMock.setIdLista(45L);
        requestDTOMock.setIdTipo(1L);
    }

    @Test
    @DisplayName("GET /api/asistencias - Debe retornar status 200 y el listado enriquecido distribuido")
    void obtenerTodas_DebeRetornarStatus200YListaConsolidada() throws Exception {
        when(asistenciaService.obtenerTodas()).thenReturn(List.of(responseDTOMock));

        mockMvc.perform(get("/api/asistencias")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idAsistencia").value(101))
                .andExpect(jsonPath("$[0].fecha").value("2026-06-21"))
                .andExpect(jsonPath("$[0].tipo.nombre").value("PRESENTE"))
                .andExpect(jsonPath("$[0].datosInscripcion.idUser").value(10))
                .andExpect(jsonPath("$[0].datosInscripcion.idCurso").value(5));

        verify(asistenciaService, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("GET /api/asistencias/{id} - Debe retornar status 200 si la asistencia existe localmente")
    void obtenerPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        when(asistenciaService.obtenerPorId(101L)).thenReturn(Optional.of(responseDTOMock));

        mockMvc.perform(get("/api/asistencias/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAsistencia").value(101))
                .andExpect(jsonPath("$.tipo.nombre").value("PRESENTE"));

        verify(asistenciaService, times(1)).obtenerPorId(101L);
    }

    @Test
    @DisplayName("GET /api/asistencias/{id} - Debe retornar status 404 si el registro no se encuentra en MySQL")
    void obtenerPorId_DebeRetornarStatus404_CuandoIdNoExiste() throws Exception {
        when(asistenciaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/asistencias/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(asistenciaService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("POST /api/asistencias - Debe retornar status 201 al procesar de forma correcta un cuerpo válido")
    void crear_DebeRetornarStatus201YAsistenciaCreada() throws Exception {
        when(asistenciaService.guardar(any(AsistenciaRequestDTO.class))).thenReturn(responseDTOMock);

        mockMvc.perform(post("/api/asistencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTOMock)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAsistencia").value(101))
                .andExpect(jsonPath("$.datosInscripcion.idLista").value(45));

        verify(asistenciaService, times(1)).guardar(any(AsistenciaRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/asistencias/{id} - Debe retornar status 204 tras un borrado exitoso")
    void eliminar_DebeRetornarStatus204_CuandoIdExiste() throws Exception {
        when(asistenciaService.obtenerPorId(101L)).thenReturn(Optional.of(responseDTOMock));

        mockMvc.perform(delete("/api/asistencias/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(asistenciaService, times(1)).obtenerPorId(101L);
        verify(asistenciaService, times(1)).eliminar(101L);
    }

    @Test
    @DisplayName("POST /api/asistencias - Debe retornar 400 Bad Request si el body está vacío")
    void crear_DebeRetornar400_CuandoBodyVacio() throws Exception {
        mockMvc.perform(post("/api/asistencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
