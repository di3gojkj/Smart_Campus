package com.cur_eva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos requerido para crear o registrar una nueva evaluación de curso")
public class CursoEvaluacionRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre o estado asignado", example = "ACTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotNull(message = "El ID del curso es obligatorio")
    @Schema(description = "ID del curso asociado de forma externa", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCurso; // 🛠️ AGREGADO PARA EL MÉTODO GUARDAR

    @NotNull(message = "El ID de la evaluación es obligatorio")
    @Schema(description = "ID de la evaluación procedente de Ms_Evaluacion", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idEvaluacion; // 🛠️ AGREGADO PARA EL MÉTODO GUARDAR

    @NotBlank(message = "La fecha de apertura es obligatoria")
    @Schema(description = "Fecha programada para la apertura pública", example = "2026-06-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fApertura;

    @NotBlank(message = "La fecha de cierre es obligatoria")
    @Schema(description = "Fecha límite registrada para el cierre", example = "2026-07-20", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fCierre;
}

