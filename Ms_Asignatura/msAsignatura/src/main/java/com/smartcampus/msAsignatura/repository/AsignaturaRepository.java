package com.smartcampus.msAsignatura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.smartcampus.msAsignatura.model.Asignatura;
import java.util.Optional;

@Repository
public interface AsignaturaRepository 
extends JpaRepository<Asignatura, Long> {
    Optional<Asignatura> findBySiglaIgnoreCase(String Sigla);

}
