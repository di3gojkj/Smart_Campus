package MS.tipo_asistencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import MS.tipo_asistencia.model.Tipo; 

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
}
