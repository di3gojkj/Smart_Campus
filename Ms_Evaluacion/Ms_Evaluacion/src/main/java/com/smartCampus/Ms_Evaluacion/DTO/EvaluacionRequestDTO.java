package com.smartCampus.Ms_Evaluacion.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar una Evaluacion")
public class EvaluacionRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Schema(
        description = "Nombre de la evaluacion",
        example = "Certamen 1",
        requiredMode = Schema.RequiredMode.REQUIRED
        )
    private String nombre;

    @NotNull(message = "El porcentaje es obligatorio")
    @PositiveOrZero(message = "El porcentaje no puede ser negativo")
    @Min(value = 1, message = "El porcentaje minimo permitido es 1")
    @Max(value = 100, message = "El porcentaje maximo permitido es 100")
    @Schema(
        description = "Porcentaje de ponderacion",
        example = "30.0",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double porcentaje;

    @NotNull(message = "El ID del tipo de evaluacion es obligatorio")
    @Schema(
        description = "ID del tipo de evaluacion",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idTipoEval;
}
