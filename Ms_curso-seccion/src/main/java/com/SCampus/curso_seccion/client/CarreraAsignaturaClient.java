package com.SCampus.curso_seccion.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.SCampus.curso_seccion.dto.CarreraAsignaturaResponseDTO;

@FeignClient(name = "ms-carrera", url = "http://localhost:8081/api/carrera-asignaturas")
public interface CarreraAsignaturaClient {

    @GetMapping("/carrera/{idCarrera}")
    List<CarreraAsignaturaResponseDTO> listarPorCarrera(@PathVariable("idCarrera") Long idCarrera);
}
