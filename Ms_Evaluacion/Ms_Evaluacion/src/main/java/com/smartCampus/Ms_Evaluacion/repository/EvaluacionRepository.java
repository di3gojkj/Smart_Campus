package com.smartCampus.Ms_Evaluacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartCampus.Ms_Evaluacion.model.Evaluacion;

@Repository
public interface EvaluacionRepository 
extends JpaRepository<Evaluacion,Long>{

    /** Valida si existe una evaluación por nombre (Ignora Mayúsculas/Minúsculas) */
    boolean existsByNombreIgnoreCase(String nombre);

    // Filtro por nombre (parcial) y porcentaje mínimo
    @Query("SELECT e FROM Evaluacion e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "AND e.porcentaje >= :min")
    List<Evaluacion> findByNameAndMinPorcentaje(@Param("nombre") String nombre, @Param("min") Double min);

    // Validar duplicados para edición (excluyendo el id actual)
    @Query("SELECT COUNT(e) > 0 FROM Evaluacion e WHERE LOWER(e.nombre) = LOWER(:nombre) " +
           "AND e.tipoEvaluacion.id_Tipo_Evaluacion = :idTipo AND e.id_Evaluacion <> :idExcluir")
    boolean existsByNameAndTipoExcludingId(@Param("nombre") String nombre, 
                                          @Param("idTipo") Long idTipo, 
                                          @Param("idExcluir") Long idExcluir);

    //Busca por el tipo de evaluacion
    @Query("SELECT e FROM Evaluacion e WHERE e.tipoEvaluacion.id_Tipo_Evaluacion = :id")
    List<Evaluacion> findByTipo(@Param("id") Long id);
}
