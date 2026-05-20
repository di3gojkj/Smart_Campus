package com.smartCampus.Ms_Carrera.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartCampus.Ms_Carrera.DTO.AsignaturaResponseDTO;

@FeignClient(name = "ms-asignatura", url = "${url.ms-asignatura}")
public interface AsignaturaClient {

    @GetMapping("/api/asignaturas/{id}")
    AsignaturaResponseDTO obtenerAsignaturaPorId(@PathVariable Long id);
}
