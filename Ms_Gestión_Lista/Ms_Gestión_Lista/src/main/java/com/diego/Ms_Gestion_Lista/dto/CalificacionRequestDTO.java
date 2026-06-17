package com.diego.Ms_Gestion_Lista.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Objeto para registrar una nota en una lista")
public class CalificacionRequestDTO {
    @NotNull(message = "La nota es requerida")
    @DecimalMin(value = "1.0", message = "La nota mínima en la escala es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima en la escala es 7.0")
    @Schema(description = "Nota obtenida", example = "6.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal nota;

    @NotNull(message = "Debe asociar la nota a un ID de lista válido")
    @Schema(description = "ID de la inscripción del alumno", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idLista;

    @NotNull(message = "El ID de la ponderación/evaluación del curso es requerido")
    @Schema(description = "ID de la evaluación", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCurEva;
}