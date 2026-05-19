package com.smartcampus.msAsignatura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartcampus.msAsignatura.model.Semestre;

@Repository
public interface SemestreRepository 
extends JpaRepository<Semestre, Long> {

    /* En esta consulta se busca el nombre del semestre por la variable 'nombre' ignorando Mayus y Minus */
    Optional<Semestre> findByNombreIgnoreCase(String nombre);

    /* En esta consulta JPQL: Se ordenan los semestres cronologicamente del 2026-1, 2026-2 */
    /* Nota que apuntamos a la clase "Semestre" y al atributo de Java "s.nombre" */

    @Query("SELECT s FROM Semestre s ORDER BY s.nombre ASC")
    List<Semestre> listarSemestreCronologicos();

}
