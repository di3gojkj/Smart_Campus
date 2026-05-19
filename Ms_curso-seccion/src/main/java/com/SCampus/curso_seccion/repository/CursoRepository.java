package com.SCampus.curso_seccion.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SCampus.curso_seccion.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
   
    List<Curso>findByCursos(Long id);
   
    @Query("SELECT 1 FROM Curso WHERE 1.seccion.id = :seccionId")
    List<Curso> encontrarCursoPorSeccion(@Param("seccionId")Long cursoId);

}
