package com.smartCampus.Ms_Evaluacion.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoEvaluacionRequestDTO {

    @NotBlank(message = "El nombre del tipo de evaluación no puede estar en blanco")
    @Size(min = 3, max = 50, message = "El nombre del tipo debe tener entre 3 y 50 caracteres")
    private String nombreTipo;

}