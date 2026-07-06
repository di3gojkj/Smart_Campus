package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
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
    private GestionListaClient listaClient;

    @InjectMocks
    private AsistenciaService asistenciaService;

    private Asistencia asistenciaMock;
    private Tipo tipoMock;
    private AsistenciaRequestDTO requestDTOMock;
    private ListaResponseDTO listaResponseMock;

    @BeforeEach
    void setUp() {
        tipoMock = new Tipo();
        tipoMock.setIdTipo(1L);
        tipoMock.setNombre("PRESENTE");

        asistenciaMock = new Asistencia();
        asistenciaMock.setIdAsistencia(101L);
        asistenciaMock.setFecha("2026-06-21");
        asistenciaMock.setIdLista(45L);
        asistenciaMock.setTipo(tipoMock);

        requestDTOMock = new AsistenciaRequestDTO();
        requestDTOMock.setFecha("2026-06-21");
        requestDTOMock.setIdLista(45L);
        requestDTOMock.setIdTipo(1L);

        listaResponseMock = new ListaResponseDTO();
        listaResponseMock.setIdLista(45L);
        listaResponseMock.setIdUser(10L);
        listaResponseMock.setIdCurso(5L);
    }

    @Test
    @DisplayName("obtenerTodas() - Debe retornar DTOs con los metadatos externos de inscripción inyectados con éxito")
    void obtenerTodas_DebeRetornarListaEnriquecida_CuandoEcosistemaResponde() {
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaMock));
        when(listaClient.buscarPorId(45L)).thenReturn(listaResponseMock);

        List<AsistenciaResponseDTO> resultado = asistenciaService.obtenerTodas();

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
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaMock));
        when(listaClient.buscarPorId(45L)).thenThrow(new RuntimeException("Error HTTP 500 Interno"));

        List<AsistenciaResponseDTO> resultado = asistenciaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2026-06-21", resultado.get(0).getFecha());
        assertNull(resultado.get(0).getDatosInscripcion());
        
        verify(asistenciaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardar() - Debe almacenar con éxito tras pasar la validación dual local y distribuida")
    void guardar_DebePersistir_CuandoCatalogoYListaExisten() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));
        when(listaClient.buscarPorId(45L)).thenReturn(listaResponseMock);
        when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(asistenciaMock);

        AsistenciaResponseDTO resultado = asistenciaService.guardar(requestDTOMock);

        assertNotNull(resultado);
        assertEquals(101L, resultado.getIdAsistencia());
        assertNotNull(resultado.getDatosInscripcion());

        verify(tipoRepository, times(1)).findById(1L);
        verify(listaClient, times(2)).buscarPorId(45L);
        verify(asistenciaRepository, times(1)).save(any(Asistencia.class));
    }

    @Test
    @DisplayName("guardar() - Debe lanzar RuntimeException si el catálogo paramétrico de tipo local no existe")
    void guardar_DebeLanzarExcepcion_CuandoTipoLocalInexistente() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.empty());

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
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoMock));
        
        FeignException.NotFound feignNotFound = mock(FeignException.NotFound.class);
        when(listaClient.buscarPorId(45L)).thenThrow(feignNotFound);

        assertThrows(RuntimeException.class, () -> {
            asistenciaService.guardar(requestDTOMock);
        });

        verify(tipoRepository, times(1)).findById(1L);
        verify(listaClient, times(1)).buscarPorId(45L);
        verify(asistenciaRepository, never()).save(any(Asistencia.class));
    }

    @Test
    @DisplayName("obtenerPorId() - Debe retornar el DTO consolidado si existe localmente")
    void obtenerPorId_DebeRetornarDTO_CuandoExiste() {
        when(asistenciaRepository.findById(101L)).thenReturn(Optional.of(asistenciaMock));
        when(listaClient.buscarPorId(45L)).thenReturn(listaResponseMock);

        Optional<AsistenciaResponseDTO> resultado = asistenciaService.obtenerPorId(101L);

        assertTrue(resultado.isPresent());
        assertEquals(101L, resultado.get().getIdAsistencia());
        verify(asistenciaRepository, times(1)).findById(101L);
        verify(listaClient, times(1)).buscarPorId(45L);
    }

    @Test
    @DisplayName("obtenerPorId() - Debe retornar Optional vacío si no existe en BD")
    void obtenerPorId_DebeRetornarVacio_CuandoNoExiste() {
        when(asistenciaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<AsistenciaResponseDTO> resultado = asistenciaService.obtenerPorId(999L);

        assertTrue(resultado.isEmpty());
        verify(asistenciaRepository, times(1)).findById(999L);
        verify(listaClient, never()).buscarPorId(any());
    }

    @Test
    @DisplayName("eliminar() - Debe llamar al repositorio para borrar el registro")
    void eliminar_DebeLlamarAlRepositorio() {
        // Ejecutamos el método void
        asistenciaService.eliminar(101L);
        
        // Verificamos que pasó por la línea de código correcta
        verify(asistenciaRepository, times(1)).deleteById(101L);
    }
}
