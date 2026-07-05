package MS.tipo_asistencia.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias desde cero para AsistenciaRepository")
public class AsistenciaRepositoryTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    private Asistencia asistenciaEjemplo;

    @BeforeEach
    void setUp() {
        Tipo tipoPresente = new Tipo();
        tipoPresente.setIdTipo(1L);
        tipoPresente.setNombre("PRESENTE");

        asistenciaEjemplo = new Asistencia();
        asistenciaEjemplo.setIdAsistencia(1L);
        asistenciaEjemplo.setFecha("2026-06-21");
        asistenciaEjemplo.setIdLista(45L);
        asistenciaEjemplo.setTipo(tipoPresente);
    }

    @Test
    @DisplayName("findAll() - Debe retornar el listado completo de asistencias persistidas localmente")
    void findAll_DebeRetornarTodasLasAsistencias() {
        when(asistenciaRepository.findAll()).thenReturn(List.of(asistenciaEjemplo));

        List<Asistencia> resultado = asistenciaRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("2026-06-21", resultado.get(0).getFecha());
        assertEquals(45L, resultado.get(0).getIdLista());
        assertEquals("PRESENTE", resultado.get(0).getTipo().getNombre());
    }

    @Test
    @DisplayName("findById() - Debe recuperar correctamente la asistencia por su clave primaria")
    void findById_DebeRetornarAsistencia_CuandoIdExiste() {
        when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistenciaEjemplo));

        Optional<Asistencia> resultado = asistenciaRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("2026-06-21", resultado.get().getFecha());
        assertEquals(1L, resultado.get().getTipo().getIdTipo());
    }

    @Test
    @DisplayName("findById() - Debe retornar un Optional vacío si el identificador no existe en MySQL")
    void findById_DebeRetornarVacio_CuandoIdNoExiste() {
        when(asistenciaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Asistencia> resultado = asistenciaRepository.findById(999L);
        assertFalse(resultado.isPresent());
    }
}
