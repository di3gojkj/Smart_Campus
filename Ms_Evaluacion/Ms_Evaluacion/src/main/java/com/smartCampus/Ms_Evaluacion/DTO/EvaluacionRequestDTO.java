package com.smartCampus.Ms_Evaluacion.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EvaluacionRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El porcentaje es obligatorio")
    @PositiveOrZero(message = "El porcentaje no puede ser negativo")
    @Min(value = 1, message = "El porcentaje mínimo permitido es 1")
    @Max(value = 100, message = "El porcentaje máximo permitido es 100")
    private Double porcentaje;

    @NotNull(message = "El ID del tipo de evaluación es obligatorio")
    private Long idTipoEval;
}
