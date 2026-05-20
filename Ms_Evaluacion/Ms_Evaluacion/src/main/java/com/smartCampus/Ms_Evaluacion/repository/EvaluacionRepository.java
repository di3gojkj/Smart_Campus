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

    // Buscar por tipo con join optimizado
    @Query("SELECT e FROM Evaluacion e JOIN FETCH e.tipoEval t WHERE t.idTipoEval = :idTipo")
    List<Evaluacion> findByTipo(@Param("idTipo") Long idTipo);

    // Filtro por nombre (parcial) y porcentaje mínimo
    @Query("SELECT e FROM Evaluacion e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "AND e.porcentaje >= :min")
    List<Evaluacion> findByNameAndMinPorcentaje(@Param("nombre") String nombre, @Param("min") Double min);

    // Validar duplicados para edición (excluyendo el id actual)
    @Query("SELECT COUNT(e) > 0 FROM Evaluacion e WHERE LOWER(e.nombre) = LOWER(:nombre) " +
           "AND e.tipoEval.idTipoEval = :idTipo AND e.idEval <> :idExcluir")
    boolean existsByNameAndTipoExcludingId(@Param("nombre") String nombre, 
                                          @Param("idTipo") Long idTipo, 
                                          @Param("idExcluir") Long idExcluir);

    
}
