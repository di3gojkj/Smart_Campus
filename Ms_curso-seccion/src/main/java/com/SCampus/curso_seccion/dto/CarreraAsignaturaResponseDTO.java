package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Mapeo local de la relación Carrera-Asignatura procedente de Ms_Carrera")
public class CarreraAsignaturaResponseDTO {
    private Long idCarreraAsignatura;
    private Long idCarrera;
    private Long idAsignatura;
    private Long idSemestre;
    private String nombreAsignatura;
    private String nombreSemestre;
}
