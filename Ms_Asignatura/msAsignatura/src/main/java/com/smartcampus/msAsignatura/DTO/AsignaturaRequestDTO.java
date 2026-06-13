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
    description = "DTO Para crear o actualizar asignatura"
)
public class AsignaturaRequestDTO {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(
        description = "Nombre de la asignatura",
        example = "Desarrollo en Fullstack",
        requiredMode =Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotBlank(message = "La sigla académica es obligatoria")
    @Size(max = 10, message = "La sigla no puede superar los 10 caracteres")
    @Schema(
        description = "Sigla académica de la asignatura",
        example = "INF-230",
        requiredMode =Schema.RequiredMode.REQUIRED
    )
    private String sigla;

    /*  Recibe el ID que apunta al otro microservicio de Gestion-Estados */
    @NotNull(message = "El ID de estado es obligatorio")
    @Schema(
        description = "ID del estado asociado a la asignatura",
        example = "1",
        requiredMode =Schema.RequiredMode.REQUIRED
    )
    private Long idEstado;
    
}
