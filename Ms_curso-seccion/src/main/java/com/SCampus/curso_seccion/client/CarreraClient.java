package com.SCampus.curso_seccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.SCampus.curso_seccion.dto.CarreraResponseDTO;

@FeignClient(name = "Ms-Carrera", url = "http://localhost:8060")
public interface CarreraClient {

    // Asegúrate de colocar la ruta exacta de tu @RestController en Ms_Carrera
    @GetMapping("/api/carreras/{idCarrera}")
    CarreraResponseDTO obtenerCarreraPorId(@PathVariable("idCarrera") Long idCarrera);
}

