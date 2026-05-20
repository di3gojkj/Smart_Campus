package com.smartCampus.Ms_Carrera.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarreraRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La sigla es obligatoria")
    @Size(min = 2, max = 10, message = "La sigla debe tener entre 2 y 10 caracteres")
    private String sigla;

    @NotNull(message = "El ID del estado es obligatorio")
    @Min(value = 1, message = "El ID del estado debe ser un número positivo")
    private Long idEstado;
}
