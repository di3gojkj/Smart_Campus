package com.SCampus.curso_seccion.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SCampus.curso_seccion.model.Curso;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByFechaCreacion(String fechaCreacion);
}
