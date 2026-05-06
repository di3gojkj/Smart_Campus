package com.smartcampus.msAsignatura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcampus.msAsignatura.model.Semestre;

@Repository
public interface SemestreRepository 
extends JpaRepository<Semestre, Long> {

    Optional<Semestre> findByNombreSemestre(String nombre_semestre);

}
