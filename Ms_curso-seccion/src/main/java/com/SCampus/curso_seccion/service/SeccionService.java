package com.SCampus.curso_seccion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.repository.SeccionRepository;

@Service
public class SeccionService {
    @Autowired
    private SeccionRepository seccionRepository;

    //metodo para obtener todas las secciones
    public List<Seccion> obtenerTodas(){
        return seccionRepository.findAll();
    }
    //metodo para obtener una seccion mediante su id
    public Optional<Seccion> obtenerPorId(Long id){
        return seccionRepository.findById(id);
    }

    //metodo para crear una nueva seccion
    public Seccion guardar(Seccion sec){
        return seccionRepository.save(sec);
    }

    //metodo para eliminar una seccion
    public void eliminar(Long id){
        seccionRepository.deleteById(id);
    }
}
