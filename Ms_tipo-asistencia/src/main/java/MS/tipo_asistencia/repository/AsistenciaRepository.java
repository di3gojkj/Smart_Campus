package MS.tipo_asistencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import MS.tipo_asistencia.model.Asistencia; 

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}