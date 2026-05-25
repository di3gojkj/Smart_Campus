package com.diego.Ms_Gestion_Lista.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import com.diego.Ms_Gestion_Lista.dto.UsuarioResponseDTO;

// Apunta directamente al puerto 8084 del MS Gestión de Usuarios
@FeignClient(name = "MS-Gestion-Usuario", url = "http://localhost:8084")

public interface UsuarioClient {
    
    @GetMapping("/api/usuarios/{id}")
    UsuarioResponseDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}
