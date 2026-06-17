package com.diego.Ms_Gestion_Lista.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Objeto para registrar a un usuario en un curso")
public class ListaRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    @Schema(description = "ID del Estudiante", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idUser;

    @NotNull(message = "El ID de curso es obligatorio")
    @Schema(description = "ID del Curso", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idCurso;
}