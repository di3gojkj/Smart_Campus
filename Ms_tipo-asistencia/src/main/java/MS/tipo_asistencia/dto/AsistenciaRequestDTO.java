package MS.tipo_asistencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class AsistenciaRequestDTO {
    @NotBlank(message = "La fecha no puede estar vacía")
    private String fecha;

     @NotNull(message = "El tipoId es obligatorio")
    private Long tipoId;
}
