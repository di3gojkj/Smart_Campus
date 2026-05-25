package com.cur_eva.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoEvaluacionResponseDTO {
    private Long idCursoEvaluacion;
    private String nombre;
    private String fCreacion;
    private String fCierre;
    private String fApertura;
}
