package com.smartCampus.Ms_Carrera.Client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartCampus.Ms_Carrera.DTO.EstadoResponseDTO;

@FeignClient(name = "Ms-Gestion-Estado", url = "http://localhost:8080")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")    

    EstadoResponseDTO obtenerEstadoPorId(@PathVariable("id") Long id);

}
