package Ms.confi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import Ms.confi.dto.UsuarioDTO;

@FeignClient(name = "MS-Gestion-Usuario", url = "${ms.usuario.url}")
public interface UsuarioClient {

    // Cambiar a endpoints específicos del MS de Usuarios si fuese necesario para buscar por correo
    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);

    @GetMapping("/api/usuarios/correo/{correo}")
    UsuarioDTO obtenerUsuarioPorCorreo(@PathVariable("correo") String correo);
}