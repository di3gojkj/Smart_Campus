package MS.tipo_asistencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import MS.tipo_asistencia.model.Tipo;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
    
    @Query("SELECT t FROM Tipo t WHERE t.idTipo = :idTipo")
    List<Tipo> findByTipo(@Param("idTipo") long idTipo);

}
