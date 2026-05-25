package Ms.confi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String clave; 
}