package com.diego.Ms_Gestion_Lista.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.diego.Ms_Gestion_Lista.model.Calificacion;
import com.diego.Ms_Gestion_Lista.model.Lista;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de CalificacionRepository (H2)")
public class CalificacionRepositoryTest {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private ListaRepository listaRepository;

    private Lista listaGuardada;

    @BeforeEach
    void setUp() {
        // Para guardar una calificación, primero debemos tener una Lista (por la Foreign Key)
        Lista lista = new Lista(null, 10L, 5L, LocalDateTime.now());
        listaGuardada = listaRepository.save(lista);
    }

    @Test
    @DisplayName("Debe guardar una Calificación correctamente")
    void guardarCalificacion_debeRetornarId() {
        Calificacion nuevaCalificacion = new Calificacion(null, new BigDecimal("6.5"), listaGuardada, 2L);
        Calificacion guardada = calificacionRepository.save(nuevaCalificacion);

        assertNotNull(guardada.getIdCalificacion());
        assertEquals(new BigDecimal("6.5"), guardada.getNota());
    }

    @Test
    @DisplayName("buscarPorLista() debe encontrar las notas de esa lista")
    void buscarPorLista_debeRetornarListaDeNotas() {
        Calificacion calif1 = new Calificacion(null, new BigDecimal("6.0"), listaGuardada, 1L);
        Calificacion calif2 = new Calificacion(null, new BigDecimal("7.0"), listaGuardada, 2L);
        calificacionRepository.save(calif1);
        calificacionRepository.save(calif2);

        // Aquí usamos el método personalizado que tienes en tu interfaz
        List<Calificacion> resultados = calificacionRepository.buscarPorLista(listaGuardada.getIdLista());

        assertEquals(2, resultados.size());
    }
}