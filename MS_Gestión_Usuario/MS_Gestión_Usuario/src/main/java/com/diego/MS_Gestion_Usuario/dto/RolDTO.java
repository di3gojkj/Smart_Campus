package com.diego.MS_Gestion_Usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de transferencia ligero para representar un Rol dentro de un usuario")
public class RolDTO {

    @Schema(description = "Identificador del rol", example = "1")
    private Long idRol;

    @Schema(description = "Nombre del privilegio", example = "ADMINISTRADOR")
    private String nombre;
}