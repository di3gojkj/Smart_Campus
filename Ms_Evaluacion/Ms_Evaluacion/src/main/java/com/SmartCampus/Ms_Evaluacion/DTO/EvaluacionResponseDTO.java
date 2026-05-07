package com.SmartCampus.Ms_Evaluacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EvaluacionResponseDTO {

    private Long idEvaluacion;
    private String nombre;
    private String descripcion;
    private String tipoNombre;
}
