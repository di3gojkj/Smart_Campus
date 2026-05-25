package com.smartCampus.Ms_Evaluacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.smartCampus.Ms_Evaluacion.model.TipoEvaluacion;

@Repository
public interface TipoEvaluacionRepository 
extends JpaRepository <TipoEvaluacion, Long>{

    // Validar existencia
    boolean existsByNombreTipoIgnoreCase(String nombreTipo);

    // Buscar por nombre
    Optional<TipoEvaluacion> findByNombreTipoIgnoreCase(String nombreTipo);

    // Conteo de evaluaciones por tipo
}
