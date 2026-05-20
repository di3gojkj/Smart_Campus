package com.smartCampus.Ms_Evaluacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionResponseDTO {

    private Long id_Evaluacion;
    private String nombre;
    private Double porcentaje;
    private Long idTipoEval;
    private String nombreTipo;

}
