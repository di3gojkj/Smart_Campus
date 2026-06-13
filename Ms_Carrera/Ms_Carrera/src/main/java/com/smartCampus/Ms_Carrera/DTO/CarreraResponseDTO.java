package com.smartCampus.Ms_Carrera.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(
    description = "DTO para retornar datos de la carrera al cliente"
)
public class CarreraResponseDTO {

    @Schema(description = "Id unico generado por la BD", 
     example = "1",
     accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idCarrera;

    @Schema(
        description = "Nombre de la carrera",
        example = "Ingenieria en Informatica"
    )
    private String nombre;

    @Schema(
        description = "Id del micro servicio de Gestion Estado ",
        example = "1"
    )
    private String sigla;

    @Schema(
        description = "Id del microservicio de Gestion Estado ",
        example = "1l"
    )
    private Long idEstado;
}
