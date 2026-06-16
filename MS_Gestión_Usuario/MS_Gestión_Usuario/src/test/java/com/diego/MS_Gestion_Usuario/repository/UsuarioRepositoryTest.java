package com.diego.MS_Gestion_Usuario.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.diego.MS_Gestion_Usuario.model.Usuario;

// Inyectamos las propiedades aquí mismo para forzar el Dialecto H2 e ignorar a MySQL
@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test del repositorio de usuarios en memoria (H2)")
public class UsuarioRepositoryTest {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Usuario usuarioActivo;
    private Usuario usuarioInactivo;

    @BeforeEach
    void setUp() {
        // entityManager.persistAndFlush guarda el dato temporalmente en la RAM
        // Se usa un HashSet vacío para los roles para simplificar la prueba de la entidad base
        usuarioActivo = entityManager.persistAndFlush(
            new Usuario(null, "12345678-9", "Diego", "Rivas", "diego@duocuc.cl", "clave123", 1L, new HashSet<>())
        );
        
        usuarioInactivo = entityManager.persistAndFlush(
            new Usuario(null, "98765432-1", "Brandon", "Perez", "brandon@duocuc.cl", "clave456", 2L, new HashSet<>())
        );
    }

    // TEST para findByCorreo() cuando SI existe
    @Test
    @DisplayName("findByCorreo() debe retornar Optional con el usuario cuando existe")
    void findByCorreo_debeRetornarUsuario_cuandoExiste() {
        // Ejecución
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("diego@duocuc.cl");

        // Criterios de aceptación
        assertTrue(resultado.isPresent());
        assertEquals("Diego", resultado.get().getNombre());
        assertEquals("12345678-9", resultado.get().getRut());
    }

    // TEST para findByCorreo() cuando NO existe
    @Test
    @DisplayName("findByCorreo() debe retornar Optional vacio cuando el correo no existe")
    void findByCorreo_debeRetornarVacio_cuandoNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findByCorreo("fantasma@duocuc.cl");

        assertFalse(resultado.isPresent());
    }

    // TEST para tu Query personalizada buscarUsuariosPorEstado()
    @Test
    @DisplayName("buscarUsuariosPorEstado() debe retornar la lista filtrada por el ID de estado")
    void buscarUsuariosPorEstado_debeRetornarListaDeUsuariosFiltrada() {
        // Ejecución: Buscamos solo a los que tienen idEstado = 1L (Activos)
        List<Usuario> resultado = usuarioRepository.buscarUsuariosPorEstado(1L);

        // Criterios de aceptación
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Diego", resultado.get(0).getNombre());
    }

}
