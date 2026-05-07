package com.SmartCampus.Ms_Evaluacion.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EvaluacionRequestDTO {


    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;
    
    @Size(max = 200, message = "La descripcion no puede superar los 200 caracteres")
    private String descripcion;

    @NotNull(message = "El ID del tipo evaluacion es obligatorio")
    private long idTipoEvaluacion;
}
