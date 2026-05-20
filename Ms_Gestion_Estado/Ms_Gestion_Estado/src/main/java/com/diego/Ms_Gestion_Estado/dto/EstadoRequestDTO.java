package com.diego.Ms_Gestion_Estado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoRequestDTO {
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 50, message = "El nombre del estado no puede superar los 50 caracteres")
    private String nombre;
}
