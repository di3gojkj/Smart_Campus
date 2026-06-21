package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que expone la relación Curso-Evaluación con metadatos enriquecidos externamente")
public class CursoEvaluacionResponseDTO {

    @Schema(description = "ID de la relación local Curso-Evaluación", example = "1")
    private Long idCursoEvaluacion;

    @Schema(description = "ID del curso asociado", example = "12")
    private Long idCurso;

    @Schema(description = "ID de la evaluación externa", example = "1")
    private Long idEvaluacion;

  
    @Schema(description = "Nombre asignado a la evaluación del curso", example = "Evaluación Parcial Java")
    private String nombre;

    @Schema(description = "Fecha de creación del registro institucional", example = "2026-06-15")
    private String fCreacion;


    @Schema(description = "Fecha de apertura de la evaluación", example = "14/06/26")
    private String fApertura;

    @Schema(description = "Fecha límite de cierre de la evaluación", example = "20/06/26")
    private String fCierre;

   
    @Schema(description = "Nombre de la evaluación (Obtenido de Ms_Evaluacion)", example = "Certamen 1")
    private String nombreEvaluacion;

    @Schema(description = "Porcentaje de ponderación de la evaluación", example = "30.0")
    private Double porcentajeEvaluacion;

    @Schema(description = "ID del tipo de evaluación", example = "2")
    private Long idTipoEval;

    @Schema(description = "Nombre del tipo de evaluación (Obtenido de Ms_Evaluacion)", example = "Certamen")
    private String nombreTipoEvaluacion;

    @Schema(description = "Fecha de creación del curso (Obtenida de curso_seccion)", example = "20/06/26")
    private String fechaCreacionCurso;

    @Schema(description = "Nombre oficial del curso (Obtenido de curso_seccion)", example = "Programación Orientada a Objetos")
    private String nombreCurso;
}

