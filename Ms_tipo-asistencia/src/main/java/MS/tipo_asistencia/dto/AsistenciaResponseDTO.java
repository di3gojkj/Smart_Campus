package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "AsistenciaResponseDTO", 
    description = "Modelo de datos enviado por el servidor con la información detallada de la asistencia"
)
public class AsistenciaResponseDTO {

    @Schema(
        description = "Identificador único del registro de asistencia en la base de datos", 
        example = "15"
    )
    private Long idAsistencia;

    @Schema(
        description = "Fecha en la que se tomó la asistencia", 
        example = "2026-06-14"
    )
    private String fecha;

    @Schema(
        description = "Detalles extendidos del tipo de asistencia asociado (catálogo)"
    )
    private TipoResponseDTO tipo;
}


