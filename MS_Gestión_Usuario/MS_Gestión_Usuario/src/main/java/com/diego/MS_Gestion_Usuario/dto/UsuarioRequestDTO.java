package com.diego.MS_Gestion_Usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de transferencia de datos utilizado para registrar un nuevo usuario")
public class UsuarioRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Schema(description = "RUT chileno sin puntos y con guión", example = "12345678-9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Diego", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del usuario", example = "Rivas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un formato de correo válido")
    @Schema(description = "Correo institucional de Duoc UC", example = "diego.rivas@duocuc.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    @Schema(description = "Contraseña en texto plano (será encriptada por el servicio)", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clave;

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "ID del estado inicial (1 para Activo)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idEstado;

    @NotEmpty(message = "Debe tener al menos un rol asignado")
    @Schema(description = "Arreglo con los IDs de los roles que tendrá el usuario", example = "[1]", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> idsRoles;
}
