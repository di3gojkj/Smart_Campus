package com.diego.Ms_Gestion_Lista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.diego.Ms_Gestion_Lista.model.Calificacion;
import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    // Consulta JPQL limpia con la Entidad mapeada correctamente (Calificacion)
    @Query("SELECT c FROM Calificacion c WHERE c.lista.idLista = :idLista")
    List<Calificacion> buscarPorLista(@Param("idLista") Long idLista);
}
