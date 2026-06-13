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
    description = "DTO Para retornar datos de semestre cronológico al cliente"
)
public class SemestreResponseDTO {
    
    @Schema(
        description = "ID único del semestre", 
        example = "1", 
        accessMode = Schema.AccessMode.READ_ONLY)
    private Long idSemestre;

    @Schema(
        description = "Nombre identificador del semestre", 
        example = "2026-1")
    private String nombre;

    @Schema(
        description = "ID referencial del estado (Microservicio Gestión Estados)", 
        example = "1")
    private Long idEstado; 

    @Schema(
        description = "Nombre descriptivo del estado obtenido del MS Estados", 
        example = "VIGENTE")
    private String nombreEstado;

    @JsonProperty("ACTIVO") 
    @Schema(
        description = "Estado de actividad lógico del semestre", 
        example = "true")
    private boolean activo;

}
