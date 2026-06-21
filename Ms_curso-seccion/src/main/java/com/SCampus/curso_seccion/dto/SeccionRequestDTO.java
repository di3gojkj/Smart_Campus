package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos requerido para la creación de una sección académica")
public class SeccionRequestDTO {

    @NotBlank(message = "El nombre de la sección es mandatorio")
    @Schema(description = "Nombre asignado a la sección", example = "Sección B", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotNull(message = "El ID del curso es obligatorio")
    @Schema(description = "Identificador único del curso asociado a la sección", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cursoId;
    
    
}