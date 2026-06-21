package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Modelo de datos requerido para registrar o actualizar una asistencia")
public class AsistenciaRequestDTO {

    @NotBlank(message = "La fecha de asistencia es obligatoria")
    @Schema(description = "Fecha de registro diario (AAAA-MM-DD)", example = "2026-06-21", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fecha;

    @NotNull(message = "El ID de la lista/inscripción es obligatorio")
    @Schema(description = "ID de la inscripción correspondiente en gestion_lista", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idLista;

    @NotNull(message = "El ID del tipo de asistencia es obligatorio")
    @Schema(description = "Identificador del catálogo paramétrico de tipo de asistencia", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idTipo;
}

