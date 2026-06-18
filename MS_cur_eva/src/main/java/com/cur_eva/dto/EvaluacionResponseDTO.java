package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "EvaluacionResponse", 
    description = "Modelo espejo que representa la estructura de datos devuelta por el microservicio remoto Ms_Evaluacion"
)
public class EvaluacionResponseDTO {

    @Schema(
        description = "Identificador único de la evaluación en la base de datos de Ms_Evaluacion", 
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id_Evaluacion;

    @Schema(
        description = "Nombre descriptivo asignado a la actividad evaluativa", 
        example = "Examen Parcial I",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
        description = "Porcentaje o peso de la evaluación sobre la nota final del curso", 
        example = "25.5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double porcentaje;

    @Schema(
        description = "Identificador único del tipo de evaluación asociado", 
        example = "2"
    )
    private Long idTipoEval;

    @Schema(
        description = "Nombre o categoría textual del tipo de evaluación", 
        example = "Práctica Teórica"
    )
    private String nombreTipo;
}
