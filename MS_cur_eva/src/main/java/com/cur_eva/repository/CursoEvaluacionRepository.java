package com.cur_eva.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cur_eva.model.CursoEvaluacion;

@Repository
public interface CursoEvaluacionRepository extends JpaRepository<CursoEvaluacion, Long> {

    // Busca estados por su nombre ignorando mayúsculas/minúsculas de forma automática
    Optional<CursoEvaluacion> findByNombreIgnoreCase(String nombre);

    // Consulta JPQL corregida que apunta a tu entidad de persistencia Java 'CursoEvaluacion'
    @Query("SELECT c FROM CursoEvaluacion c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    Optional<CursoEvaluacion> buscarPorNombreExacto(@Param("nombre") String nombre);
}

