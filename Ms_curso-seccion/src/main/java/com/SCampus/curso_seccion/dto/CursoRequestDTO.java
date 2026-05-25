package com.SCampus.curso_seccion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoRequestDTO {
    
    @NotBlank(message = "La fecha de creación es obligatoria")
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{2}$", message = "La fecha debe tener la estructura estricta DD/MM/AA")
    private String fechaCreacion;
}