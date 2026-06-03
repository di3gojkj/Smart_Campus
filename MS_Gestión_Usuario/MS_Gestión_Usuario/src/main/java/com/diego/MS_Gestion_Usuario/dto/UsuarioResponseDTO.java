package com.diego.MS_Gestion_Usuario.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private Long idEstado;
    private Set<RolDTO> roles;
    private String clave;
}
