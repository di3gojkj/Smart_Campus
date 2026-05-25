package com.diego.MS_Gestion_Usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.diego.MS_Gestion_Usuario.dto.EstadoResponseDTO;
import java.util.List;

@FeignClient(name = "MS-Gestion-Usuario", url = "http://localhost:8083")
public interface EstadoClient {

    // Contrato estricto para validar un estado individual por ID
    @GetMapping("/api/estados/{id}")
    EstadoResponseDTO obtenerEstadoPorId(@PathVariable("id") Long id);

    // Contrato alternativo en caso de requerir listas de control desde el MS Estado
    @GetMapping("/api/estados")
    List<EstadoResponseDTO> obtenerTodosLosEstados();
}

