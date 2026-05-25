package MS.tipo_asistencia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class TipoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    public Long getIdTipo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdTipo'");
    }

}
