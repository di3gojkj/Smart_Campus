package MS.tipo_asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Schema(
    name = "TipoResponseDTO", 
    description = "Modelo de datos enviado por el servidor que representa la clasificación del catálogo de asistencia"
)
public class TipoResponseDTO {

    @Schema(
        description = "Identificador único de la clasificación de asistencia en la base de datos", 
        example = "1"
    )
    private Long idTipo;

    @Schema(
        description = "Nombre asignado a la categoría de asistencia", 
        example = "PRESENTE"
    )
    private String nombre;
}