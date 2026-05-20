package com.smartCampus.Ms_Carrera.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraAsignaturaRequestDTO {

    @NotNull(message = "El ID de la carrera es obligatorio")
    private Long idCarrera;

    @NotNull(message = "El ID de la asignatura es obligatorio")
    private Long idAsignatura;

    @NotNull(message = "El ID del semestre es obligatorio")
    private Long idSemestre;

}
