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

import MS.tipo_asistencia.dto.AsistenciaRequestDTO;
import MS.tipo_asistencia.dto.AsistenciaResponseDTO;
import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo; // CORREGIDO: Importa 'Tipo'
import MS.tipo_asistencia.repository.AsistenciaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test Unit de AsistenciaService")
public class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @InjectMocks
    private AsistenciaService asistenciaService;

    private Tipo tipoPresente; // CORREGIDO: Tipo
    private Asistencia asistenciaEjemplo;
    @BeforeEach
    void setUp() {
        tipoPresente = new Tipo(1L, "PRESENTE"); // CORREGIDO: Instanciación limpia de Tipo
        asistenciaEjemplo = new Asistencia(15L, "2026-06-14", tipoPresente);
        new AsistenciaRequestDTO("2026-06-14", 1L);
    }

    @Test
    @DisplayName("obtenerTodas() retorna la lista de DTO de todas las asistencias")
    void obtenerTodas_debeRetornarListaDeAsistencias() {
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaEjemplo));
        List<AsistenciaResponseDTO> resultado = asistenciaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(asistenciaRepository, times(1)).findAll();
    }
}

