package com.smartCampus.Ms_Evaluacion.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta para Evaluacion")
public class EvaluacionResponseDTO {

    @Schema(
        description = "Id unico de la evaluacion",
         example = "1"
        )
    private Long idEvaluacion;

    @Schema(
        description = "Nombre de la evaluacion",
        example = "Certamen 1"
    )
    private String nombre;

    @Schema(
        description = "Porcentaje de ponderacion",
        example = "30.0"
    )
    private Double porcentaje;

    @Schema(
        description = "Id del tipo de evaluacion",
        example = "1"
    )
    private Long idTipoEval;

    @Schema(
        description = "Nombre del tipo de evaluacion",
        example = "Certamen"
    )
    private String nombreTipo;
}
