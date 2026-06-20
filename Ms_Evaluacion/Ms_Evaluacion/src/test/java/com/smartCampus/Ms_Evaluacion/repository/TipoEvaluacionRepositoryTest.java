package com.smartCampus.Ms_Evaluacion.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de integracion: TipoEvaluacionRepository")
public class TipoEvaluacionRepositoryTest {

    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private TipoEvaluacion tipo1;
    private TipoEvaluacion tipo2;

    @BeforeEach
    void setUp(){
        tipo1 = entityManager.persistAndFlush(
            new TipoEvaluacion(null, "Certamen",null));
        
        tipo2 = entityManager.persistAndFlush(
            new TipoEvaluacion(null, "Control",null));
    }

    /* 1. TEST PARA findAll() */
    @Test
    @DisplayName("findAll() debe retornar todos los tipos de evaluación de la BD")
    void findAll_debeRetornarTodosLosTipos() {
        List<TipoEvaluacion> tipos = tipoEvaluacionRepository.findAll();
        assertNotNull(tipos);
        assertEquals(2, tipos.size());
    }

    /* 2. TEST PARA findById() */
    @Test
    @DisplayName("findById() debe retornar Optional con el tipo cuando existe")
    void findById_debeRetornarTipo_cuandoExiste() {
        Optional<TipoEvaluacion> resultado = tipoEvaluacionRepository.findById(tipo1.getIdTipoEval());
        assertTrue(resultado.isPresent());
        assertEquals("Certamen", resultado.get().getNombreTipo());
    }

    /* 3. TEST PARA existsByNombreTipoIgnoreCase() */
    @Test
    @DisplayName("existsByNombreTipoIgnoreCase() debe retornar true si el tipo existe ignorando mayúsculas")
    void existsByNombreTipoIgnoreCase_debeRetornarTrue_cuandoExiste() {
        boolean resultado = tipoEvaluacionRepository.existsByNombreTipoIgnoreCase("certamen");
        assertTrue(resultado);
    }

    /* 4. TEST PARA findByNombreTipoIgnoreCase() */
    @Test
    @DisplayName("findByNombreTipoIgnoreCase() debe retornar el objeto encapsulado en un Optional")
    void findByNombreTipoIgnoreCase_debeRetornarTipoEvaluacion() {
        Optional<TipoEvaluacion> resultado = tipoEvaluacionRepository.findByNombreTipoIgnoreCase("control");
        assertTrue(resultado.isPresent());
        assertEquals("Control", resultado.get().getNombreTipo());
    }



}
