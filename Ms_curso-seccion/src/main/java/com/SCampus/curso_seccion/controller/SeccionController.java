package com.SCampus.curso_seccion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SCampus.curso_seccion.model.Seccion;
import com.SCampus.curso_seccion.service.SeccionService;

import jakarta.validation.Valid;

@RestController
//la url base para acceder a este archivo
@RequestMapping("/api/seccion")
public class SeccionController {
     @Autowired
    private SeccionService seccionService;

    //endpoints accesibles para este controlador

    //metodo GET -> permite obtener y mostrar datos (SELECT)
    @GetMapping()
    public ResponseEntity<List<Seccion>> obtenerSecciones(){
        return ResponseEntity.ok(seccionService.obtenerTodas());
    }
    //endpoint para obtener solo las categorias que cumplan una condicion
    //@PathVariable extrae ese {id} de la URL
    @GetMapping("/{id}")
    public ResponseEntity<Seccion> obtenerSeccionPorId(@PathVariable Long id){
        return seccionService.obtenerPorId(id)
        .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    //@RequestBody --> recibe los datos mediante el body del http
    @PostMapping()
    public ResponseEntity<Seccion> crear(@Valid @RequestBody Seccion cat){
        Seccion nueva = seccionService.guardar(cat);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seccion> actualizar(
        @PathVariable Long id,
        @Valid @RequestBody Seccion cat
    ){
        //verificar la categoria con el id existe
        return seccionService.obtenerPorId(id)
        .map(existente -> { //si existe, mediante una funcion de flecha le digo que hacer
            cat.setId(id); //aseguramos que se actualice el correcto
            return ResponseEntity.ok(seccionService.guardar(cat));
        })
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if(seccionService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        seccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
