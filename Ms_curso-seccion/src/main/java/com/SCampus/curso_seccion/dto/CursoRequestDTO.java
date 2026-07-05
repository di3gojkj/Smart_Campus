package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos requerido para registrar un nuevo curso")
public class CursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Schema(description = "Nombre oficial de la asignatura", example = "Programación Orientada a Objetos")
    private String nombre;

    @NotBlank(message = "La fecha de creación es obligatoria")
    @Schema(description = "Fecha de alta del curso", example = "14/06/26")
    private String fechaCreacion;
}
