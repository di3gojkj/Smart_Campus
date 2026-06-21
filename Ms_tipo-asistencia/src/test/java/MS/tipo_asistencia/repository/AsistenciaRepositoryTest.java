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

import MS.tipo_asistencia.model.Asistencia;
import MS.tipo_asistencia.model.Tipo;
import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("Pruebas Unitarias desde cero para AsistenciaRepository")
public class AsistenciaRepositoryTest {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private EntityManager entityManager;

    private Tipo tipoPresente;
    private Asistencia asistenciaEjemplo;

    @BeforeEach
    void setUp() {
        // 1. Persistimos la entidad paramétrica local obligatoria para el mapa de base de datos
        Tipo tipo = new Tipo();
        tipo.setNombre("PRESENTE");
        entityManager.persist(tipo);
        tipoPresente = tipo;

        // 2. Registramos la asistencia vinculando el catálogo guardado y un ID de lista simulado
        asistenciaEjemplo = new Asistencia();
        asistenciaEjemplo.setFecha("2026-06-21");
        asistenciaEjemplo.setIdLista(45L); // ID de inscripción simulada de gestion_lista
        asistenciaEjemplo.setTipo(tipoPresente);

        entityManager.persist(asistenciaEjemplo);
        entityManager.flush();
    }

    @Test
    @DisplayName("findAll() - Debe retornar el listado completo de asistencias persistidas localmente")
    void findAll_DebeRetornarTodasLasAsistencias() {
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
        Optional<Asistencia> resultado = asistenciaRepository.findById(asistenciaEjemplo.getIdAsistencia());

        assertTrue(resultado.isPresent());
        assertEquals("2026-06-21", resultado.get().getFecha());
        assertEquals(tipoPresente.getIdTipo(), resultado.get().getTipo().getIdTipo());
    }

    @Test
    @DisplayName("findById() - Debe retornar un Optional vacío si el identificador no existe en MySQL")
    void findById_DebeRetornarVacio_CuandoIdNoExiste() {
        Optional<Asistencia> resultado = asistenciaRepository.findById(999L);
        assertFalse(resultado.isPresent());
    }
}
