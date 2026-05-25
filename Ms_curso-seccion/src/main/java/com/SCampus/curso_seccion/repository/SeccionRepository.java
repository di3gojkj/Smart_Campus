package com.SCampus.curso_seccion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SCampus.curso_seccion.model.Seccion;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByIdCurso(Long idCurso);
    Optional<Seccion> findByNombreIgnoreCase(String nombre);
}