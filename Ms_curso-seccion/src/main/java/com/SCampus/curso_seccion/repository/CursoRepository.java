package com.SCampus.curso_seccion.repository;

import java.util.Optional; // IMPORTACIÓN REQUERIDA

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SCampus.curso_seccion.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    // CORREGIDO: Se cambia List por Optional para que sea 100% compatible con el .isPresent() de tu Servicio
    Optional<Curso> findByFechaCreacion(String fechaCreacion);
}

