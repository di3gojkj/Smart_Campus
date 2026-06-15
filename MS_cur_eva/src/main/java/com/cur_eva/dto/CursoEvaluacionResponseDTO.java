package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "CursoEvaluacionResponse", 
    description = "Modelo de datos que representa la respuesta del servidor con la información de la evaluación"
)
public class CursoEvaluacionResponseDTO {

    @Schema(
        description = "Identificador único autogenerado de la evaluación en la base de datos", 
        example = "1"
    )
    private Long idCursoEvaluacion;

    @Schema(
        description = "Nombre o estado actual de la evaluación", 
        example = "ACTIVO"
    )
    private String nombre;

    @Schema(
        description = "Fecha en la que se creó el registro", 
        example = "2026-06-15"
    )
    private String fCreacion;

    @Schema(
        description = "Fecha límite en la que se cerró o cerrará la evaluación", 
        example = "2026-07-20"
    )
    private String fCierre;

    @Schema(
        description = "Fecha en la que se abrió o abrirá la evaluación", 
        example = "2026-06-20"
    )
    private String fApertura;
}
