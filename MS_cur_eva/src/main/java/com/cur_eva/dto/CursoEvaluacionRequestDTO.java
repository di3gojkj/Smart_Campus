package com.cur_eva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoEvaluacionRequestDTO {
    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 50, message = "El nombre del estado no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "la fecha de creacion es obligatorio")
    @Size(max = 50, message = "la fecha no puede superar los 50 caracteres")
    private String fCreacion;

    @NotBlank(message = "la fecha de cierre es obligatorio")
    @Size(max = 50, message = "la fecha no puede superar los 50 caracteres")
    private String fCierre;

    @NotBlank(message = "la fecha de apertura es obligatorio")
    @Size(max = 50, message = "la fecha no puede superar los 50 caracteres")
    private String fApertura;
}
