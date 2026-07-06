package com.diego.Ms_Gestion_Lista.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.diego.Ms_Gestion_Lista.model.Lista;

@DataJpaTest(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE"
})
@ActiveProfiles("test")
@DisplayName("Test de ListaRepository (H2)")
public class ListaRepositoryTest {

    @Autowired
    private ListaRepository listaRepository;

    @Test
    @DisplayName("Debe guardar un registro de Lista y generarle un ID")
    void guardarLista_debeRetornarId() {
        Lista nuevaLista = new Lista(null, 10L, 5L, LocalDateTime.now());
        Lista guardada = listaRepository.save(nuevaLista);

        assertNotNull(guardada.getIdLista());
        assertEquals(10L, guardada.getIdUser());
        assertEquals(5L, guardada.getIdCurso());
    }

    @Test
    @DisplayName("findByIdUser() debe retornar las listas de un usuario")
    void buscarPorUser_debeEncontrarListas() {
        listaRepository.save(new Lista(null, 15L, 5L, LocalDateTime.now()));
        List<Lista> resultados = listaRepository.findByIdUser(15L);
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("findByIdCurso() debe retornar las listas de un curso")
    void buscarPorCurso_debeEncontrarListas() {
        listaRepository.save(new Lista(null, 10L, 25L, LocalDateTime.now()));
        List<Lista> resultados = listaRepository.findByIdCurso(25L);
        assertEquals(1, resultados.size());
    }
}