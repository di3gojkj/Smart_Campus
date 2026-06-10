package com.smartCampus.Ms_Carrera.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraAsignaturaRequestDTO {

    @NotNull(message = "El ID de la carrera es obligatorio")
    @Schema(description = "ID de la carrera",
     example = "1",
     requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCarrera;

    @NotNull(message = "El ID de la asignatura es obligatorio")
    @Schema(description = "ID de la asignatura (via microservicio)",
     example = "5",
     requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idAsignatura;

    @NotNull(message = "El ID del semestre es obligatorio")
    @Schema(description = "ID del semestre (via microservicio)",
     example = "1",
     requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idSemestre;

}
