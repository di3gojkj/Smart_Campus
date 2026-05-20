package com.smartCampus.Ms_Carrera.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraAsignaturaResponseDTO {

    private Long idCarreraAsignatura;
    private Long idCarrera;
    private Long idAsignatura;
    private Long idSemestre;

    // Campos de enriquecimiento (lo que llenamos con los otros MS)
    private String nombreAsignatura;
    private String nombreSemestre;

}
