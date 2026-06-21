package MS.tipo_asistencia.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import MS.tipo_asistencia.model.Tipo;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("Pruebas Paramétricas desde cero para TipoRepository")
public class TipoRepositoryTest {

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private EntityManager entityManager;

    private Tipo tipoBase;

    @BeforeEach
    void setUp() {
        // Inicializamos una clasificación paramétrica respetando las restricciones de tu modelo físico
        tipoBase = new Tipo();
        tipoBase.setNombre("JUSTIFICADO");

        entityManager.persist(tipoBase);
        entityManager.flush();
    }

    @Test
    @DisplayName("findAll() - Debe retornar el catálogo completo de clasificaciones registradas")
    void findAll_DebeRetornarTodosLosTiposDeAsistencia() {
        List<Tipo> resultado = tipoRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("JUSTIFICADO", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("findById() - Debe recuperar exitosamente una clasificación paramétrica por su clave primaria")
    void findById_DebeRetornarTipo_CuandoIdExiste() {
        Optional<Tipo> resultado = tipoRepository.findById(tipoBase.getIdTipo());

        assertTrue(resultado.isPresent());
        assertEquals("JUSTIFICADO", resultado.get().getNombre());
        assertEquals(tipoBase.getIdTipo(), resultado.get().getIdTipo());
    }

    @Test
    @DisplayName("findById() - Debe retornar un Optional vacío si el identificador no existe en MySQL")
    void findById_DebeRetornarVacio_CuandoIdNoExiste() {
        Optional<Tipo> resultado = tipoRepository.findById(999L);
        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("save() - Debe registrar una nueva clasificación en el catálogo de manera correcta")
    void save_DebePersistirNuevoTipo_CuandoSeProcesaEntidad() {
        Tipo nuevoTipo = new Tipo();
        nuevoTipo.setNombre("INASISTENCIA_INJUSTIFICADA");

        Tipo guardado = tipoRepository.save(nuevoTipo);

        assertNotNull(guardado.getIdTipo());
        assertEquals("INASISTENCIA_INJUSTIFICADA", guardado.getNombre());
        
        // Verificamos que se haya incrementado el total en el repositorio local
        assertEquals(2, tipoRepository.count());
    }
}
