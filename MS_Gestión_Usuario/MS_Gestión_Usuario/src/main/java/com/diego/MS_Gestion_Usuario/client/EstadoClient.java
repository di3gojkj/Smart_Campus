package com.diego.MS_Gestion_Usuario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// Se conecta al puerto 8083 (MS Estado)
@FeignClient(name = "estado", url = "${ms.estado.url}")
public interface EstadoClient {
    // Finge ser el controlador del otro microservicio
    @GetMapping("/api/estados/{id}")
    Object obtenerEstadoPorId(@PathVariable("id") Long id);
}

