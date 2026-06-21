package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Mapeo local del objeto de respuesta que devuelve los datos de la inscripción procedente de gestion_lista")
public class ListaResponseDTO {
    
    @Schema(description = "ID autogenerado de la lista remota", example = "1")
    private Long idLista;
    
    @Schema(description = "ID del usuario (Estudiante) asociado", example = "10")
    private Long idUser;
    
    @Schema(description = "ID del curso inscrito", example = "5")
    private Long idCurso;
    
    @Schema(
        description = "Fecha exacta en la que se generó la inscripción", 
        example = "2026-06-17T10:30:00",
        type = "string",
        format = "date-time"
    )
    private LocalDateTime fechaCreacion;
}
