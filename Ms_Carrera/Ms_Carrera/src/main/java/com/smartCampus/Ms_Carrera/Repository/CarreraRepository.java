package com.smartCampus.Ms_Carrera.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartCampus.Ms_Carrera.model.Carrera;

import org.springframework.data.repository.query.Param; 

@Repository
public interface CarreraRepository extends
 JpaRepository <Carrera, Long> {
    
    /*Verifica la existencia de una carrera por la sigla, para evitar dupes */

    Optional<Carrera> findBySigla(String sigla);

    /*Busqueda flexible ya sea por Nombre o Sigla */
    @Query("SELECT c FROM Carrera c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))" +
        "OR LOWER(c.sigla) LIKE LOWER (CONCAT('%',:filtro,'%'))")
    List<Carrera> buscarPorNombreOSigla(@Param("filtro") String filtro);

    /*Excluir el registro en validaciones de actulizacion*/
    @Query("SELECT c FROM Carrera c WHERE c.sigla = :sigla AND c.idCarrera <> :idCarrera")
    Optional<Carrera> findBySiglaExcludingCurrent(@Param("sigla") String sigla, @Param("idCarrera") Long idCarrera);
}
