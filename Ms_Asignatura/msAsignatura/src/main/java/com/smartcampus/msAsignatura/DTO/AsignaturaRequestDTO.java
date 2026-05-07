package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AsignaturaRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre de la asignatura no puede superar los 50 caracteres")
    private String nombre_asigntura;

    @NotBlank(message = "La sigla es obligatoria")
    @Size(max = 10, message = "La sigla no puede superar los 10 caracteres")
    private String sigla;

    @NotNull(message = "El idEstado es obligatorio")
    @Positive(message = "El idEstado debe ser un número positivo")
    private Long idEstado;
    
}
