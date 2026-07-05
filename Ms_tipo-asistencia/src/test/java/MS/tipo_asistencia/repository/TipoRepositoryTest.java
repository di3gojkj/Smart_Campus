package MS.tipo_asistencia.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS.tipo_asistencia.model.Tipo;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Paramétricas desde cero para TipoRepository")
public class TipoRepositoryTest {

    @Mock
    private TipoRepository tipoRepository;

    private Tipo tipoBase;

    @BeforeEach
    void setUp() {
        tipoBase = new Tipo();
        tipoBase.setIdTipo(1L);
        tipoBase.setNombre("JUSTIFICADO");
    }

    @Test
    @DisplayName("findAll() - Debe retornar el catálogo completo de clasificaciones registradas")
    void findAll_DebeRetornarTodosLosTiposDeAsistencia() {
        when(tipoRepository.findAll()).thenReturn(List.of(tipoBase));

        List<Tipo> resultado = tipoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("JUSTIFICADO", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findById() - Debe recuperar exitosamente una clasificación paramétrica por su clave primaria")
    void findById_DebeRetornarTipo_CuandoIdExiste() {
        when(tipoRepository.findById(1L)).thenReturn(Optional.of(tipoBase));

        Optional<Tipo> resultado = tipoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("JUSTIFICADO", resultado.get().getNombre());
        assertEquals(1L, resultado.get().getIdTipo());
    }

    @Test
    @DisplayName("findById() - Debe retornar un Optional vacío si el identificador no existe en MySQL")
    void findById_DebeRetornarVacio_CuandoIdNoExiste() {
        when(tipoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Tipo> resultado = tipoRepository.findById(999L);
        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("save() - Debe registrar una nueva clasificación en el catálogo de manera correcta")
    void save_DebePersistirNuevoTipo_CuandoSeProcesaEntidad() {
        Tipo nuevoTipo = new Tipo();
        nuevoTipo.setIdTipo(2L);
        nuevoTipo.setNombre("INASISTENCIA_INJUSTIFICADA");

        when(tipoRepository.save(any(Tipo.class))).thenReturn(nuevoTipo);
        when(tipoRepository.count()).thenReturn(2L);

        Tipo guardado = tipoRepository.save(nuevoTipo);

        assertNotNull(guardado.getIdTipo());
        assertEquals("INASISTENCIA_INJUSTIFICADA", guardado.getNombre());
        assertEquals(2, tipoRepository.count());
    }
}
