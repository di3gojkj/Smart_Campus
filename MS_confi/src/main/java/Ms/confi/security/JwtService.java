package Ms.confi.security;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class JwtService {

    public String generarToken(String correo) {
        // Generación de identificador único compacto simulando firma JWT
        return UUID.randomUUID().toString() + "-" + correo;
    }
}