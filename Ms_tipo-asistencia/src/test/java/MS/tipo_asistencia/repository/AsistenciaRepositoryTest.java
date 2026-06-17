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

import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo; // CORREGIDO: Importa 'Tipo'

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de registros de asistencia en memoria H2")
public class AsistenciaRepositoryTest {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Asistencia asistenciaDia1;
    private Tipo tipoPresente; // CORREGIDO: Tipo

    @BeforeEach
    void setUp() {
        tipoPresente = entityManager.persistAndFlush(new Tipo(null, "PRESENTE"));
        asistenciaDia1 = entityManager.persistAndFlush(new Asistencia(null, "2026-06-14", tipoPresente));
    }

    @Test
    @DisplayName("findAll() debe retornar los pases de lista registrados")
    void findAll_debeRetornarTodasLasAsistencias() {
        List<Asistencia> lista = asistenciaRepository.findAll();
        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}
