package com.smartcampus.msAsignatura.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AsignaturaRequestDTO {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La sigla académica es obligatoria")
    @Size(max = 10, message = "La sigla no puede superar los 10 caracteres")
    private String sigla;

    /*  Recibe el ID que apunta al otro microservicio de Gestion-Estados */
    @NotNull(message = "El ID de estado es obligatorio")
    private Long idEstado;
    
}
