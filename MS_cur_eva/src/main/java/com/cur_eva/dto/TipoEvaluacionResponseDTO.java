package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Mapeo local de la respuesta de Tipo de Evaluación procedente de Ms_Evaluacion")
public class TipoEvaluacionResponseDTO {

    @Schema(description = "Id único del tipo de evaluación", example = "1")
    private Long idTipoEval;

    @Schema(description = "Nombre del tipo de evaluación", example = "Certamen")
    private String nombreTipo;
}
