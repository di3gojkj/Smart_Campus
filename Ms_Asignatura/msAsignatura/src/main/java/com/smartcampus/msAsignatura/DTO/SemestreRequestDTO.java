package com.smartcampus.msAsignatura.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(
    description = "DTO Para crear o actualizar semestre cronológico"
)
public class SemestreRequestDTO {
    @NotBlank(message = "El nombre del semestre es obligatorio (Ej: 2026-1)")
    @Size(min = 6, max = 50, message = "El nombre del semestre debe tener entre 6 y 50 caracteres")
    @Schema(
        description = "Nombre identificador del semestre cronológico",
        example = "2026-1", 
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    /*  Recibe el ID que apunta al otro microservicio de Gestion-Estados */
    @NotNull(message = "El ID de estado es obligatorio")
    @Schema(
        description = "ID del estado provisto por el microservicio de Estados", 
        example = "1", 
        requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idEstado;
}
