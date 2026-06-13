package com.smartCampus.Ms_Carrera.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(
    description = "DTO Para retornar datos de asignatura al cliente"
)
public class AsignaturaResponseDTO {

    @Schema(
        description = "ID único de la asignatura",
        example = "1", 
        accessMode = Schema.AccessMode.READ_ONLY)
    private Long idAsignatura; 

    @Schema(
        description = "Nombre de la asignatura",
        example = "Estructuras de Datos")
    private String nombre;

    @Schema(
        description = "Sigla académica de la asignatura",
        example = "INF-230"
    )
    private String sigla;

    @Schema(
        description = "ID referencial del estado (Microservicio Gestión Estados)",
        example = "1"
    )
    private Long idEstado; 

    @Schema(
        description = "Nombre descriptivo del estado obtenido del MS Estados",
        example = "ACTIVO")
    private String nombreEstado; 
    
    @JsonProperty("ACTIVO") 
    @Schema(
        description = "Indica si la asignatura está disponible en el periodo actual", 
        example = "true")
    private boolean activo; 
}

