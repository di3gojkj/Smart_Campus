package Ms.confi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico no válido")
    private String correo;

    @NotBlank(message = "La contraseña es requerida")
    private String clave;
}