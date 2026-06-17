package com.diego.Ms_Gestion_Estado.repository;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("Debe guardar un estado y generarle un ID")
    void guardarEstado_debeRetornarEstadoConId() {
        Estado estado = new Estado(null, "ACTIVO");
        Estado guardado = estadoRepository.save(estado);

        assertNotNull(guardado.getIdEstado());
        assertEquals("ACTIVO", guardado.getNombre());
    }
}
