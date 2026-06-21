package com.SCampus.curso_seccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos devuelto tras consultar o registrar un curso")
public class CursoResponseDTO {

    @Schema(description = "Identificador único del curso en la base de datos", example = "12")
    private Long id;

    @Schema(description = "Nombre oficial de la asignatura", example = "Programación Orientada a Objetos")
    private String nombre;

    @Schema(description = "Fecha de registro del curso", example = "20/06/26")
    private String fechaCreacion;

    
}