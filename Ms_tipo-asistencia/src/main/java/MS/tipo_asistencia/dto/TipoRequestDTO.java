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
@Schema(
    name = "TipoRequestDTO", 
    description = "Modelo de datos requerido para agregar o modificar una clasificación dentro del catálogo de asistencia"
)
public class TipoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(
        description = "Nombre descriptivo de la categoría de asistencia", 
        example = "PRESENTE", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotNull(message = "El tipoId es obligatorio")
    @Schema(
        description = "Código identificador numérico manual asignado al tipo de asistencia", 
        example = "1", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long tipoId;
}