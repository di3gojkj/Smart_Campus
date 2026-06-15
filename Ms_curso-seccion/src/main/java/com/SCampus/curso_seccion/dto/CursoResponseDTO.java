package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "Curso", 
    description = "Modelo de datos que representa la información de un curso académico"
)
public class CursoResponseDTO {

    @Schema(
        description = "Identificador único autogenerado del curso", 
        example = "12",
        readOnly = true
    )
    private Long id;

    @Schema(
        description = "Fecha en la que se dio de alta el curso (formato sugerido: AAAA-MM-DD)", 
        example = "2026-03-10",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String fechaCreacion;
}