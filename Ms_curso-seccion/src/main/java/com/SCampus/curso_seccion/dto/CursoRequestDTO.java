package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "CursoRequest", 
    description = "Modelo de datos requerido para registrar un nuevo curso en el sistema"
)
public class CursoRequestDTO {
    
    @NotBlank(message = "La fecha de creación es obligatoria")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{2}$", message = "La fecha debe tener la estructura estricta DD/MM/AA")
    @Schema(
        description = "Fecha de alta del curso. Debe cumplir estrictamente con el formato de dos dígitos para día, mes y año.", 
        example = "14/06/26",
        pattern = "^\\d{2}/\\d{2}/\\d{2}$",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fechaCreacion;
}