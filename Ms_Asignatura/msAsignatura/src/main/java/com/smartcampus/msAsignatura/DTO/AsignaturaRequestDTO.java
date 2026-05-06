package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class AsignaturaRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "La sigla es obligatoria")
    private String sigla;
    @NotNull(message = "El idEstado es obligatorio")
    @Positive(message = "El idEstado debe ser un número positivo")
    private Long idEstado;
    
}
