package MS.tipo_asistencia.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo;
import MS.tipo_asistencia.repository.TipoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de TipoService")
public class TipoServiceTest {

    @Mock
    private TipoRepository tipoRepository;

    @InjectMocks
    private TipoService tipoService;

    private Tipo tipoEjemplo;
    private TipoRequestDTO requestDtoMock;

    @BeforeEach
    void setUp() {
        tipoEjemplo = new Tipo(1L, "PRESENTE");
        
        requestDtoMock = new TipoRequestDTO();
        requestDtoMock.setNombre("PRESENTE");
        requestDtoMock.setTipoId(1L);
    }

    @Test
    @DisplayName("obtenerTodas() retorna la lista de DTO")
    void obtenerTodas_debeRetornarListaDeTipos() {
        when(tipoRepository.findAll()).thenReturn(List.of(tipoEjemplo));
        List<TipoResponseDTO> resultado = tipoService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tipoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId() retorna el DTO si existe")
    void obtenerPorId_debeRetornarTipo_CuandoIdExiste() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoEjemplo));
        TipoResponseDTO resultado = tipoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("PRESENTE", resultado.getNombre());
        verify(tipoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId() lanza excepcion si no existe")
    void obtenerPorId_debeLanzarExcepcion_CuandoIdNoExiste() {
        when(tipoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            tipoService.obtenerPorId(999L);
        });

        verify(tipoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("crear() persiste y retorna el DTO")
    void crear_debePersistirNuevoTipo() {
        when(tipoRepository.save(any(Tipo.class))).thenReturn(tipoEjemplo);
        TipoResponseDTO resultado = tipoService.crear(requestDtoMock);

        assertNotNull(resultado);
        assertEquals("PRESENTE", resultado.getNombre());
        verify(tipoRepository, times(1)).save(any(Tipo.class));
    }

    @Test
    @DisplayName("actualizar() modifica el registro si existe")
    void actualizar_debeModificarRegistro_CuandoIdExiste() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoEjemplo));
        when(tipoRepository.save(any(Tipo.class))).thenReturn(tipoEjemplo);
        TipoResponseDTO resultado = tipoService.actualizar(1L, requestDtoMock);

        assertNotNull(resultado);
        verify(tipoRepository, times(1)).findById(1L);
        verify(tipoRepository, times(1)).save(any(Tipo.class));
    }

    @Test
    @DisplayName("actualizar() lanza excepcion si el ID no existe")
    void actualizar_debeLanzarExcepcion_CuandoIdNoExiste() {
        when(tipoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            tipoService.actualizar(999L, requestDtoMock);
        });

        verify(tipoRepository, times(1)).findById(999L);
        verify(tipoRepository, never()).save(any(Tipo.class));
    }

    @Test
    @DisplayName("eliminar() borra el registro si existe")
    void eliminar_debeBorrarRegistro_CuandoIdExiste() {
        when(tipoRepository.existsById(1L)).thenReturn(true);
        tipoService.eliminar(1L);

        verify(tipoRepository, times(1)).existsById(1L);
        verify(tipoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar() lanza excepcion si el ID no existe")
    void eliminar_debeLanzarExcepcion_CuandoIdNoExiste() {
        when(tipoRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            tipoService.eliminar(999L);
        });

        verify(tipoRepository, times(1)).existsById(999L);
        verify(tipoRepository, never()).deleteById(any(Long.class));
    }
}
