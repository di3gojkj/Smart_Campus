package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos requerido para crear o registrar un curso")
public class CursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Schema(description = "Nombre oficial de la asignatura o curso académico", example = "Programación Orientada a Objetos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "La fecha de creación es obligatoria")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{2}$", message = "La fecha debe cumplir el formato DD/MM/AA")
    @Schema(description = "Fecha en que se registra el curso en el sistema", example = "20/06/26", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaCreacion;
    
   
}