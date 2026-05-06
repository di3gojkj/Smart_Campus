package com.smartcampus.msAsignatura.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AsignaturaResponseDTO {

    private Long idAsig;
    private String nombre;
    private String sigla;
    private Long idEstado;

}
