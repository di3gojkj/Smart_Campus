package com.smartCampus.Ms_Evaluacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEvaluacionResponseDTO {

    private Long idTipoEval;
    private String nombreTipo;
}