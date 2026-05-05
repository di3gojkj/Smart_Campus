package com.diego.Ms_Gestion_Estado.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoRequestDTO {
    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombre;
}
