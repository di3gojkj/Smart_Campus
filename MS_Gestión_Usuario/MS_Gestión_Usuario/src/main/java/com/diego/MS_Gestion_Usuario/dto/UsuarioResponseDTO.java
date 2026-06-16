package com.diego.MS_Gestion_Usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta que oculta datos sensibles (como la clave) al exponer la información del usuario")
public class UsuarioResponseDTO {

    @Schema(description = "ID del usuario", example = "1")
    private Long idUsuario;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombre del usuario", example = "Diego")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Rivas")
    private String apellido;

    @Schema(description = "Correo del usuario", example = "diego.rivas@duocuc.cl")
    private String correo;

    @Schema(description = "ID del estado actual", example = "1")
    private Long idEstado;

    @Schema(description = "Lista detallada de los roles del usuario")
    private Set<RolDTO> roles;
}