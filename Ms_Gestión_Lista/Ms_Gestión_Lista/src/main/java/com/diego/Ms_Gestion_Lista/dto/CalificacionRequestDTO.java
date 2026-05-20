package com.diego.Ms_Gestion_Lista.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CalificacionRequestDTO {
    @NotNull(message = "La nota es requerida")
    @DecimalMin(value = "1.0", message = "La nota mínima en la escala es 1.0")
    @DecimalMax(value = "7.0", message = "La nota máxima en la escala es 7.0")
    private BigDecimal nota;

    @NotNull(message = "Debe asociar la nota a un ID de lista válido")
    private Long idLista;

    @NotNull(message = "El ID de la ponderación/evaluación del curso es requerido")
    private Long idCurEva;
}
