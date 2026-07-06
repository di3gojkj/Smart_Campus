package com.diego.Ms_Gestion_Estado.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.diego.Ms_Gestion_Estado.model.Estado;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de Estados (H2)")
public class EstadoRepositoryTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @BeforeEach
    void setUp() {
        estadoRepository.save(new Estado(null, "ACTIVO"));
    }

    @Test
    @DisplayName("Debe guardar un estado y generarle un ID")
    void guardarEstado_debeRetornarEstadoConId() {
        Estado guardado = estadoRepository.save(new Estado(null, "INACTIVO"));
        assertNotNull(guardado.getIdEstado());
        assertEquals("INACTIVO", guardado.getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe encontrar el estado ignorando mayúsculas")
    void findByNombreIgnoreCase_debeRetornarEstado_cuandoExiste() {
        // Buscamos en minúsculas aunque se guardó en mayúsculas
        Optional<Estado> resultado = estadoRepository.findByNombreIgnoreCase("activo");
        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getNombre());
    }

    @Test
    @DisplayName("findByNombreIgnoreCase() debe retornar Optional vacío cuando no existe")
    void findByNombreIgnoreCase_debeRetornarVacio_cuandoNoExiste() {
        Optional<Estado> resultado = estadoRepository.findByNombreIgnoreCase("fantasma");
        assertFalse(resultado.isPresent());
    }
}
