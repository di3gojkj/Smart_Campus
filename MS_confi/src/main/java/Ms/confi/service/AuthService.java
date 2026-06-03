package Ms.confi.service;

import Ms.confi.client.UsuarioClient;
import Ms.confi.dto.LoginRequest;
import Ms.confi.dto.LoginResponse;
import Ms.confi.dto.UsuarioDTO;
import Ms.confi.security.JwtService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UsuarioClient usuarioClient;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        logger.info("Iniciando proceso de autenticación para el usuario: {}", request.getCorreo());
        
        try {
            // 1. Simulación de recuperación mediante el cliente Feign (ID de prueba o ajuste por correo)
            UsuarioDTO usuario = usuarioClient.obtenerUsuarioPorCorreo(request.getCorreo()); 
            
            // 2. Validación de credenciales contra el passwordEncoder configurado en SecurityConfig
            if (!passwordEncoder.matches(request.getClave(), usuario.getClave())) {
                logger.warn("Fallo de autenticación: Contraseña incorrecta para el usuario {}", request.getCorreo());
                throw new RuntimeException("Credenciales incorrectas de acceso.");
            }
            
            String token = jwtService.generarToken(usuario.getCorreo());
            logger.info("Autenticación exitosa. Token JWT emitido con éxito.");
            return new LoginResponse(token);
            
        } catch (FeignException.NotFound ex) {
            logger.warn("El usuario centralizado no fue localizado en el MS Usuarios");
            throw new RuntimeException("El usuario ingresado no existe en los registros.");
        } catch (Exception e) {
            logger.error("Fallo crítico en la comunicación entre microservicios: {}", e.getMessage());
            throw new RuntimeException("Error de infraestructura: No se pudo verificar la identidad.");
        }
    }
}