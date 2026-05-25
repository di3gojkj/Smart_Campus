package com.smartcampus.msAsignatura.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartcampus.msAsignatura.DTO.EstadoResponseDTO;



@FeignClient(name = "Ms-Gestion-Estado", url = "http://localhost:8080")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")    

    EstadoResponseDTO obtenerEstadoPorId(@PathVariable("id") Long id);

}
