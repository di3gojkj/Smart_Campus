package com.smartCampus.Ms_Evaluacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;

public interface TipoEvaluacionRepository 
extends JpaRepository <TipoEvaluacion, Long>{

    // Validar existencia
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar por nombre
    Optional<TipoEvaluacion> findByNombreIgnoreCase(String nombre);

    // Conteo de evaluaciones por tipo
    @Query("SELECT t.nombre, COUNT(e) FROM TipoEval t LEFT JOIN t.evaluaciones e GROUP BY t.nombre")
    List<Object[]> countEvaluacionesPorTipo();
}
