package com.diego.MS_Gestion_Usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.diego.MS_Gestion_Usuario.model.Usuario;
import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    
    Optional<Usuario> findByRut(String rut);

    // Consulta JPQL para buscar usuarios que pertenezcan a un ID de estado específico
    @Query("SELECT u FROM Usuario u WHERE u.idEstado = :idEstado")
    List<Usuario> buscarUsuariosPorEstado(@Param("idEstado") Long idEstado);
}
