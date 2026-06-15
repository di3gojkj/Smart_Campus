package MS.tipo_asistencia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // IMPORTACIÓN OBLIGATORIA DE JAKARTA
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "id_tipo")
@Schema(name = "TipoEntity", description = "Catálogo paramétrico de clasificaciones de asistencia")
public class Tipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Tipo", updatable = false, nullable = false)
    @Schema(description = "Identificador único autoincrementable", example = "1", readOnly = true)
    private Long idTipo;

    @Column(nullable = false, length = 30)
    @Schema(description = "Nombre oficial de la clasificación", example = "PRESENTE", maxLength = 30)
    private String nombre;
}
