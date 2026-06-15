package MS.tipo_asistencia.service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import MS.tipo_asistencia.dto.TipoRequestDTO;
import MS.tipo_asistencia.dto.TipoResponseDTO;
import MS.tipo_asistencia.model.Tipo; // CORREGIDO: Importa 'Tipo'
import MS.tipo_asistencia.repository.TipoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de TipoService")
public class TipoServiceTest {

    @Mock
    private TipoRepository tipoRepository;

    @InjectMocks
    private TipoService tipoService;

    private Tipo tipoEjemplo; // CORREGIDO: Tipo
    @BeforeEach
    void setUp() {
        tipoEjemplo = new Tipo(1L, "PRESENTE"); // CORREGIDO: Tipo
        new TipoRequestDTO();
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
}

