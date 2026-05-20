package com.diego.Ms_Gestion_Lista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diego.Ms_Gestion_Lista.model.Lista;
import java.util.List;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Long> {
    List<Lista> findByIdUser(Long idUser);
    List<Lista> findByIdCurso(Long idCurso);
}
