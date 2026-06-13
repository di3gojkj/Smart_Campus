package com.smartCampus.Ms_Carrera.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(
    description = "DTO para retornar datos de la relacion entre carrera y asignatura al cliente"
)
public class CarreraAsignaturaResponseDTO {

    @Schema(
        description = "Id de la relacion",
        example = "1",
        accessMode =Schema.AccessMode.READ_ONLY
    )
    private Long idCarreraAsignatura;

    @Schema(
        description = "Carrera asociada"
    )
    private Long idCarrera;

    @Schema(
        description = "ID del microservicio de asignaturas", 
        example = "5"
    )
    private Long idAsignatura;

    @Schema(
        description = "Número del semestre", 
        example = "1"
    )
    private Long idSemestre;

    // Campos de enriquecimiento (lo que llenamos con los otros MS)

    @Schema(
        description = "Nombre de la asignatura (Obtenido de Ms_Asignatura)",
        example = "Desarrollo en Fullstack"
    )
    private String nombreAsignatura;

    @Schema(
        description = "Nombre de la asignatura (Obtenido de Ms_Asignatura)",
        example = "2026-1"
    )
    private String nombreSemestre;

}
