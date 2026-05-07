package com.SmartCampus.Ms_Evaluacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SmartCampus.Ms_Evaluacion.model.TipoEvaluacion;

@Repository
public interface TipoEvaluacionRepository 
extends JpaRepository<TipoEvaluacion, Long>{

    
}
