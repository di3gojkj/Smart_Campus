package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;  
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AsistenciaRequestDTO", description = "Modelo de datos requerido para registrar o actualizar una asistencia")
public class AsistenciaRequestDTO {

    @NotBlank(message = "La fecha no puede estar vacía")
    @Schema(description = "Fecha en la cual se realiza el pase de lista", example = "2026-06-14", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fecha;

    @NotNull(message = "El tipoId es obligatorio")
    @Schema(description = "ID del Tipo de Asistencia asignado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tipoId;
}
