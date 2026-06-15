package MS.tipo_asistencia.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "ErrorResponse", 
    description = "Estructura estándar utilizada por el microservicio para responder ante excepciones y errores del sistema"
)
public class ErrorResponse {

    @Schema(description = "Fecha y hora exacta en la que ocurrió el incidente", example = "2026-06-15T03:36:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código numérico del estado HTTP", example = "400")
    private int status;

    @Schema(description = "Nombre oficial del estado HTTP correspondiente", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje aclaratorio sobre el error ocurrido", example = "Errores de validación en los datos de entrada.")
    private String mensaje;

    @Schema(description = "Ruta o URI del endpoint donde se originó el fallo", example = "/api/asistencia")
    private String path;

    @Schema(
        description = "Lista detallada con cada una de las fallas de validación específicas detectadas en los campos del Request", 
        example = "[\"Campo 'fecha': La fecha no puede estar vacía\"]"
    )
    private List<String> detalles;
}