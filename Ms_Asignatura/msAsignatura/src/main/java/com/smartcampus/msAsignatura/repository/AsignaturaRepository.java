package com.smartcampus.msAsignatura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.smartcampus.msAsignatura.model.Asignatura;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignaturaRepository 
extends JpaRepository<Asignatura, Long> {


    /*Encuentra una Asignatura por su nombre */
    List<Asignatura> findByNombreContainingIgnoreCase(String nombre);
    
    /* Encuentra una asignatura por su sigla */
    Optional<Asignatura> findBySiglaIgnoreCase(String Sigla);

    /* Filtra las asignaturas por su estado Activo 'ACTIVO, INACTIVO, SUSPENDIDO'
     Usando la variable "a.idEstado"*/
    @Query("SELECT a FROM Asignatura a WHERE a.idEstado = :idEstado ORDER BY a.nombre ASC")
    List<Asignatura> buscarPorEstadoOrdenadoAlfabeticamente(@Param("idEstado") Long idEstado);

    /*Cuenta los ramos correspondientes a su estado 'ACTIVO, INACTIVO, SUSPENDIDO' */
    @Query("SELECT COUNT(a) FROM Asignatura a WHERE a.idEstado = :idEstado")
    long contarAsignaturasPorEstado(@Param("idEstado") Long idEstado);



}
