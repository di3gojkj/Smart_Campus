package com.smartCampus.Ms_Carrera.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartCampus.Ms_Carrera.DTO.SemestreResponseDTO;

@FeignClient(name = "ms-asignatura", url = "${ms-asignatura}")
public interface SemestreClient {

    @GetMapping("/api/semestres/{id}")
    SemestreResponseDTO obtenerSemestrePorId(@PathVariable Long id);

}
