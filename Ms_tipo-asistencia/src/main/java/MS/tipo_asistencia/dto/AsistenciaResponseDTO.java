package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import MS.tipo_asistencia.model.Tipo;

@Data
@Schema(description = "Modelo de datos de salida de asistencia enriquecido con metadatos del estudiante e inscripción")
public class AsistenciaResponseDTO {

    @Schema(description = "Identificador único de la asistencia local", example = "101")
    private Long idAsistencia;

    @Schema(description = "Fecha de registro de la asistencia", example = "2026-06-21")
    private String fecha;

    @Schema(description = "ID de la lista/inscripción asociada", example = "1")
    private Long idLista;

    @Schema(description = "Clasificación paramétrica de la asistencia (PRESENTE/AUSENTE)")
    private Tipo tipo;

    @Schema(description = "Metadatos detallados de la inscripción obtenidos sincrónicamente desde gestion_lista")
    private ListaResponseDTO datosInscripcion;
}



