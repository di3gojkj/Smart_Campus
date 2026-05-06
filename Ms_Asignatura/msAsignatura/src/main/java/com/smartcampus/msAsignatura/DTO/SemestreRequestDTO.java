package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SemestreRequestDTO {
    @NotBlank(message = "El nombre del semestre es obligatorio")
    private String nombre_semestre;
}
