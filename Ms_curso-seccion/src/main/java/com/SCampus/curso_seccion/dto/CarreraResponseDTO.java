package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "CarreraResponse", 
    description = "DTO espejo para recibir y mapear de forma síncrona los datos desde el microservicio Ms_Carrera"
)
public class CarreraResponseDTO {

    @Schema(
        description = "Identificador único autogenerado de la carrera en el microservicio remoto", 
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idCarrera;

    @Schema(
        description = "Nombre oficial de la carrera o programa de estudios académico", 
        example = "Ingeniería en Informática",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Schema(
        description = "Sigla o acrónimo identificativo corto de la carrera", 
        example = "INF",
        maxLength = 5
    )
    private String sigla;

    @Schema(
        description = "Identificador único del estado actual de la carrera (vínculo con Gestión Estado)", 
        example = "1"
    )
    private Long idEstado;
}


