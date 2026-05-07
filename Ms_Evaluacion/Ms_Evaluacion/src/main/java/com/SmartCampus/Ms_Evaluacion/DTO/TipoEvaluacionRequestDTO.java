package com.SmartCampus.Ms_Evaluacion.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoEvaluacionRequestDTO {

    @NotBlank(message = "El nombre del tipo de evaluacion no puede estar en blanco")
    @Size(max = 10, message = "El tipo de evaluacion no puede superar los 10 caracteres")
    private String nombreTipo;
}
