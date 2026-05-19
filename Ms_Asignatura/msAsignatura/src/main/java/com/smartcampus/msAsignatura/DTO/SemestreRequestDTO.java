package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SemestreRequestDTO {
    @NotBlank(message = "El nombre del semestre es obligatorio (Ej: 2026-1)")
    @Size(min = 6, max = 50, message = "El nombre del semestre debe tener entre 6 y 50 caracteres")
    private String nombre;

    /*  Recibe el ID que apunta al otro microservicio de Gestion-Estados */
    @NotNull(message = "El ID de estado es obligatorio")
    private Long idEstado;
}
