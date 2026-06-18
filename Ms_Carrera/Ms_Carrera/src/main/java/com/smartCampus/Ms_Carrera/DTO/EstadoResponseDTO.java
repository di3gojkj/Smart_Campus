package com.smartCampus.Ms_Carrera.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta que devuelve los datos del estado")
public class EstadoResponseDTO {

    @Schema(description = "ID del estado", example = "1")
    private Long idEstado;

    @Schema(description = "Nombre del estado", example = "ACTIVO")
    private String nombre;
}
