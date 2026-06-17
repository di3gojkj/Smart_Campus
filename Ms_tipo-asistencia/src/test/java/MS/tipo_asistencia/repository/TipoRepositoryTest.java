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
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import MS.tipo_asistencia.model.Tipo; // CORREGIDO: Importa 'Tipo'

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de tipos de asistencia en memoria H2")
public class TipoRepositoryTest {

    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Tipo presente;
    private Tipo ausente;

    @BeforeEach
    void setUp() {
        // CORREGIDO: Instanciación limpia de la clase Tipo
        presente = entityManager.persistAndFlush(new Tipo(null, "PRESENTE"));
        ausente = entityManager.persistAndFlush(new Tipo(null, "AUSENTE"));
    }

    @Test
    @DisplayName("findAll() debe retornar todos los tipos de asistencia")
    void findAll_debeRetornarTodoElCatalogo() {
        List<Tipo> tipos = tipoRepository.findAll();
        assertNotNull(tipos);
        assertEquals(2, tipos.size());
    }

    @Test
    @DisplayName("findById() debe retornar el tipo correcto")
    void findById_debeRetornarTipo_cuandoExiste() {
        Optional<Tipo> resultado = tipoRepository.findById(presente.getIdTipo());
        assertTrue(resultado.isPresent());
        assertEquals("PRESENTE", resultado.get().getNombre());
    }
}

