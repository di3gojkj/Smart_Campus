package com.diego.Ms_Gestion_Lista.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta que devuelve el detalle de una nota ingresada")
public class CalificacionResponseDTO {
    
    @Schema(description = "ID autogenerado de la calificación", example = "1")
    private Long idCalificacion;
    
    @Schema(description = "Nota obtenida por el alumno", example = "6.5")
    private BigDecimal nota;
    
    @Schema(description = "ID de la inscripción a la que pertenece la nota", example = "1")
    private Long idLista;
    
    @Schema(description = "ID de la evaluación original del curso", example = "2")
    private Long idCurEva;
}