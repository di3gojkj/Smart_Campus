package com.cur_eva.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cur_eva.model.CursoEvaluacion;


@Repository
public interface CursoEvaluacionRepository extends JpaRepository<CursoEvaluacion, Long>{
    Optional<CursoEvaluacion> findByNombreIgnoreCase(String nombre);//Metodo que Busca estados por su nombre Ignorando las mayusculas.
    //Optional se usa como buena practica para que no de un error en caso de que el nombre de estado no exista.
    
    // Consulta JPQL de ejemplo para defensas académicas (Mapea a la Entidad Estado, no a la tabla)
    @Query("SELECT e FROM Estado e WHERE LOWER(e.nombre) = LOWER(:nombre)")
    Optional<CursoEvaluacion> buscarPorNombreExacto(@Param("nombre") String nombre);

    Optional<CursoEvaluacion> findByIdCursoEvaluacionIgnoreCase(Long idCursoEvaluacion);
}
