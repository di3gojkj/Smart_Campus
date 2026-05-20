package com.smartCampus.Ms_Carrera.Client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.smartCampus.Ms_Carrera.DTO.EstadoResponseDTO;

@FeignClient(name = "ms-gestion_Estado", url = "${ms.estado.url}")
public interface EstadoClient {

    @GetMapping("/api/estados/{id}")    

    EstadoResponseDTO obtenerEstadoPorId(@PathVariable("id") Long id);

}
