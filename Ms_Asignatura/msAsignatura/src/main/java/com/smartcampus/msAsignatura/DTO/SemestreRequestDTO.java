package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SemestreRequestDTO {
    @NotBlank(message = "El nombre del semestre es obligatorio")
    @Size(max = 50, message = "El nombre del semestre no puede superar los 50 caracteres")
    private String nombre_semestre;
}
