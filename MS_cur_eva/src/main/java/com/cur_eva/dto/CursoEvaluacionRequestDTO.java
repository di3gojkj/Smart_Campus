package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "CursoEvaluacionRequest", 
    description = "Modelo de datos requerido para registrar o actualizar una evaluación de curso"
)
public class CursoEvaluacionRequestDTO {

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 10, message = "El nombre del estado no puede superar los 10 caracteres")
    @Schema(
        description = "Nombre o estado actual de la evaluación", 
        example = "ACTIVO", 
        maxLength = 10, 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotBlank(message = "la fecha de creacion es obligatorio")
    @Size(max = 10, message = "la fecha no puede superar los 10 caracteres")
    @Schema(
        description = "Fecha en la que se creó el registro del curso (formato sugerido: AAAA-MM-DD)", 
        example = "2026-06-15", 
        maxLength = 10, 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fCreacion;

    @NotBlank(message = "la fecha de cierre es obligatorio")
    @Size(max = 10, message = "la fecha no puede superar los 10 caracteres")
    @Schema(
        description = "Fecha límite programada para el cierre de la evaluación", 
        example = "2026-07-20", 
        maxLength = 10, 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fCierre;

    @NotBlank(message = "la fecha de apertura es obligatorio")
    @Size(max = 10, message = "la fecha no puede superar los 10 caracteres")
    @Schema(
        description = "Fecha planificada para la apertura de la evaluación a los estudiantes", 
        example = "2026-06-20", 
        maxLength = 10, 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fApertura;
}