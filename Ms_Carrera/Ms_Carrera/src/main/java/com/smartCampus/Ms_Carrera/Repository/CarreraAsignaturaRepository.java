package com.smartCampus.Ms_Carrera.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartCampus.Ms_Carrera.model.CarreraAsignatura;

@Repository
public interface CarreraAsignaturaRepository 
extends JpaRepository <CarreraAsignatura, Long>{
    
    // Gracias al @ManyToOne en el modelo, navegamos al ID de carrera así:
    List<CarreraAsignatura> findByCarrera_IdCarrera(Long idCarrera);

    // Validación para el Service: Para asegurar que no asignemos la misma asignatura
    // en la misma carrera y semestre dos veces.
    boolean existsByCarrera_IdCarreraAndIdAsignaturaAndIdSemestre(Long idCarrera, Long idAsignatura, Long idSemestre);
    
}




