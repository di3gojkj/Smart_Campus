package com.smartCampus.Ms_Carrera.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraResponseDTO {

    private Long idCarrera;
    private String nombre;
    private String sigla;
    private Long idEstado;
}
