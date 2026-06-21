package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Modelo de respuesta enriquecido con metadatos de la relación Carrera-Asignatura")
public class SeccionResponseDTO {

    @Schema(description = "Identificador único de la sección", example = "5")
    private Long id;

    @Schema(description = "Nombre o aula asignada a la sección", example = "Mañana - Aula 302")
    private String nombre;

    @Schema(description = "Identificador único del curso asociado localmente", example = "12")
    private Long cursoId;

    @Schema(description = "Metadatos detallados de la asignatura vinculada externamente")
    private CarreraAsignaturaResponseDTO datosAcademicos;

    @Schema(description = "Fecha y hora de registro de la sección", example = "2026-03-30T10:15:30")
    private LocalDateTime fechaCreacion;
}