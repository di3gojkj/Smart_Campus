package com.smartCampus.Ms_Evaluacion.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para crear o actualizar un Tipo de Evaluacion")
public class TipoEvaluacionRequestDTO {

    @NotBlank(message = "El nombre del tipo de evaluacion no puede estar en blanco")
    @Size(min = 3, max = 50, message = "El nombre del tipo debe tener entre 3 y 50 caracteres")
    @Schema(
        description = "Nombre del tipo de evaluacion",
        example = "Certamen", 
        requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreTipo;
}