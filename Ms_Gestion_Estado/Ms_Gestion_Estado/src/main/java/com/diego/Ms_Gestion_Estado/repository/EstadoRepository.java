package com.diego.Ms_Gestion_Estado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diego.Ms_Gestion_Estado.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {//El jpaRepository de spring jpa regala metodos CRUD: save, findAll, findById, Delete.
    Optional<Estado> findByNombreIgnoreCase(String nombre);//Metodo que Busca estados por su nombre Ignorando las mayusculas.
    //Optional se usa como buena practica para que no de un error en caso de que el nombre de estado no exista. 

}
