package com.diego.Ms_Gestion_Lista.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Apunta directamente al puerto 8084 del MS Gestión de Usuarios
//@FeignClient(name = "ms-usuario", url = "${ms.usuario.url}")
public interface UsuarioClient {
    @GetMapping("/api/usuarios/{id}")
    Object obtenerUsuarioPorId(@PathVariable("id") Long id);


}
