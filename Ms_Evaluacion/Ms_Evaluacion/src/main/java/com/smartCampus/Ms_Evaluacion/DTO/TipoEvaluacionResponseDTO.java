package com.smartCampus.Ms_Evaluacion.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta para Tipo de Evaluacion")
public class TipoEvaluacionResponseDTO {

    @Schema(
        description = "Id unico del tipo de evaluacion",
        example = "1"
    )
    private Long idTipoEval;

    @Schema(
        description = "Nombre del tipo de evaluacion",
        example = "Certamen"
    )
    private String nombreTipo;
}