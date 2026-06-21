package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import feign.FeignException;

import MS.tipo_asistencia.client.GestionListaClient;
import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.dto.ListaResponseDTO;
import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.AsistenciaRepository;
import MS.tipo_asistencia.repository.TipoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para AsistenciaService (Enlace Remoto)")
public class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private TipoRepository tipoRepository;

    @Mock
    private GestionListaClient listaClient; // 🛠️ MOCK DEL CLIENTE DE INTEGRACIÓN OPENFEIGN

    @InjectMocks
    private AsistenciaService asistenciaService;

    private Asistencia asistenciaMock;
    private Tipo tipoMock;
    private AsistenciaRequestDTO requestDTOMock;
    private ListaResponseDTO listaResponseMock;

    @BeforeEach
    void setUp() {
        // 1. Inicialización de la entidad paramétrica local
        tipoMock = new Tipo();
        tipoMock.setIdTipo(1L);
        tipoMock.setNombre("PRESENTE");

        // 2. Inicialización de la entidad principal de asistencia
        asistenciaMock = new Asistencia();
        asistenciaMock.setIdAsistencia(101L);
        asistenciaMock.setFecha("2026-06-21");
        asistenciaMock.setIdLista(45L);
        asistenciaMock.setTipo(tipoMock);

        // 3. Inicialización del DTO de entrada para persistencia
        requestDTOMock = new AsistenciaRequestDTO();
        requestDTOMock.setFecha("2026-06-21");
        requestDTOMock.setIdLista(45L);
        requestDTOMock.setIdTipo(1L);

        // 🛠️ 4. Mockeo puro con Mockito para la respuesta procedente de gestion_lista (Puerto 8095)
        listaResponseMock = mock(ListaResponseDTO.class);
        when(listaResponseMock.getIdLista()).thenReturn(45L);
        when(listaResponseMock.getIdUser()).thenReturn(10L);
        when(listaResponseMock.getIdCurso()).thenReturn(5L);
    }

    @Test
    @DisplayName("obtenerTodas() - Debe retornar DTOs con los metadatos externos de inscripción inyectados con éxito")
    void obtenerTodas_DebeRetornarListaEnriquecida_CuandoEcosistemaResponde() {
        // Arrange
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaMock));
        when(listaClient.buscarPorId(45L)).thenReturn(listaResponseMock);

        // Act
        List<AsistenciaResponseDTO> resultado = asistenciaService.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2026-06-21", resultado.get(0).getFecha());
        assertNotNull(resultado.get(0).getDatosInscripcion());
        assertEquals(10L, resultado.get(0).getDatosInscripcion().getIdUser());
        assertEquals(5L, resultado.get(0).getDatosInscripcion().getIdCurso());

        verify(asistenciaRepository, times(1)).findAll();
        verify(listaClient, times(1)).buscarPorId(45L);
    }

    @Test
    @DisplayName("obtenerTodas() - Debe ser resiliente y entregar datos locales si gestion_lista falla en la red")
    void obtenerTodas_DebeGarantizarDisponibilidad_CuandoFallaMicroservicioRemoto() {
        // Arrange
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaMock));
        when(listaClient.buscarPorId(45L)).thenThrow(new RuntimeException("Error HTTP 500 Interno"));

        // Act
        List<AsistenciaResponseDTO> resultado = asistenciaService.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2026-06-21", resultado.get(0).getFecha());
        assertNull(resultado.get(0).getDatosInscripcion()); // Resiliencia: La asistencia viaja sin metadata remota
        
        verify(asistenciaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardar() - Debe almacenar con éxito tras pasar la validación dual local y distribuida")
    void guardar_DebePersistir_CuandoCatalogoYListaExisten() {
        // Arrange
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));
        when(listaClient.buscarPorId(45L)).thenReturn(listaResponseMock);
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(asistenciaMock);

        // Act
        AsistenciaResponseDTO resultado = asistenciaService.guardar(requestDTOMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(101L, resultado.getIdAsistencia());
        assertNotNull(resultado.getDatosInscripcion());

        verify(tipoRepository, times(1)).findById(1L);
        verify(listaClient, times(2)).buscarPorId(45L); // Una en validación del guardar, otra al mapear en el toResponseDTO
        verify(asistenciaRepository, times(1)).save(any(Asistencia.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar RuntimeException si el catálogo paramétrico de tipo local no existe")
    void guardar_DebeLanzarExcepcion_CuandoTipoLocalInexistente() {
        // Arrange
        when(tipoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            asistenciaService.guardar(requestDTOMock);
        });

        verify(tipoRepository, times(1)).findById(1L);
        verify(listaClient, never()).buscarPorId(any(Long.class));
        verify(asistenciaRepository, never()).save(any(Asistencia.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar excepción perimetral si Feign reporta un 404 de la lista remota")
    void guardar_DebeLanzarExcepcion_CuandoInscripcionNoExisteEnGestionLista() {
        // Arrange
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));
        
        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(listaClient.buscarPorId(45L)).thenThrow(feignNotFound); // Bloqueo de consistencia distributed

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            asistenciaService.guardar(requestDTOMock);
        });

        verify(tipoRepository, times(1)).findById(1L);
        verify(listaClient, times(1)).buscarPorId(45L);
        verify(asistenciaRepository, never()).save(any(Asistencia.class));
    }
}
