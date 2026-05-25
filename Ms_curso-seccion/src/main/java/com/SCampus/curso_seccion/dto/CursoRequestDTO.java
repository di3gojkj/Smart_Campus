package com.SCampus.curso_seccion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoRequestDTO {
    @NotNull(message = "La fecha debe tener esta estructura: 00/00/00")
    private String fechaCreacion;
    
}
