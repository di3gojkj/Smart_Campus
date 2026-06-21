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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.TipoRepository; // O TipoService si tuvieras capa de servicio para Tipo

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para TipoController")
public class TipoControllerTest {

    private MockMvc mockMvc;

    // Nota institucional: Mockeamos el repositorio/servicio inyectado en tu TipoController real
    @Mock
    private TipoRepository tipoRepository;

    @InjectMocks
    private TipoController tipoController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Tipo tipoMock;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tipoController).build();

        // Inicializamos el objeto paramétrico de catálogo base
        tipoMock = new Tipo(1L, "JUSTIFICADO");
    }

    @Test
    @DisplayName("GET /api/tipos - Debe retornar status 200 y el listado de catálogo completo")
    void obtenerTodos_DebeRetornarStatus200YListaDeTipos() throws Exception {
        // Arrange
        when(tipoRepository.findAll()).thenReturn(List.of(tipoMock));

        // Act & Assert
        mockMvc.perform(get("/api/tipos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idTipo").value(1))
                .andExpect(jsonPath("$[0].nombre").value("JUSTIFICADO"));

        verify(tipoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/tipos/{id} - Debe retornar status 200 si la clasificación existe en el catálogo")
    void obtenerPorId_DebeRetornarStatus200_CuandoIdExiste() throws Exception {
        // Arrange
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));

        // Act & Assert
        mockMvc.perform(get("/api/tipos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipo").value(1))
                .andExpect(jsonPath("$.nombre").value("JUSTIFICADO"));

        verify(tipoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /api/tipos/{id} - Debe retornar status 404 si la clasificación no existe")
    void obtenerPorId_DebeRetornarStatus404_CuandoIdNoExiste() throws Exception {
        // Arrange
        when(tipoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tipos/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(tipoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /api/tipos - Debe retornar status 201 al registrar un nuevo tipo válido")
    void crear_DebeRetornarStatus201YTipoCreado() throws Exception {
        // Arrange
        Tipo inputDto = new Tipo();
        inputDto.setNombre("JUSTIFICADO");

        when(tipoRepository.save(any(Tipo.class))).thenReturn(tipoMock);

        // Act & Assert
        mockMvc.perform(post("/api/tipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTipo").value(1))
                .andExpect(jsonPath("$.nombre").value("JUSTIFICADO"));

        verify(tipoRepository, times(1)).save(any(Tipo.class));
    }
}
