package com.diego.MS_Gestion_Usuario.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {
    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 12, message = "El RUT no puede superar los 12 caracteres")
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un formato de correo válido")
    private String correo;

    @NotBlank(message = "La clave de acceso es obligatoria")
    @Size(min = 6, message = "La clave debe contener un mínimo de 6 caracteres")
    private String clave;

    @NotNull(message = "El identificador de estado es requerido")
    private Long idEstado;

    @NotEmpty(message = "El usuario debe poseer al menos un rol asignado")
    private Set<Long> idsRoles;

}
