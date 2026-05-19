package com.smartcampus.msAsignatura.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoResponseDTO {
    private Long idEstado;
    private String nombre;
}
