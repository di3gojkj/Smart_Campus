package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Mapeo local de los datos del Curso procedentes del microservicio curso_seccion")
public class CursoResponseDTO {

    @Schema(description = "Identificador único del curso", example = "12")
    private Long id;

    @Schema(description = "Nombre de la asignatura o curso", example = "Programación Orientada a Objetos")
    private String nombre;

    @Schema(description = "Fecha de registro del curso", example = "20/06/26")
    private String fechaCreacion;
}
