package MS.tipo_asistencia.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asistencia")
@Schema(name = "AsistenciaEntity", description = "Entidad de persistencia que representa el registro diario de asistencia escolar")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_asistencia")
    @Schema(description = "Identificador único de la asistencia", example = "101", readOnly = true)
    private Long idAsistencia;

    @Column(nullable = false, length = 150)
    @Schema(description = "Fecha de registro de la asistencia en formato texto", example = "2026-06-21")
    private String fecha;

    @Column(name = "id_lista", nullable = false)
    @Schema(description = "Identificador único de la lista de alumnos, administrado externamente por gestion_lista", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idLista; 

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    @Schema(description = "Clasificación paramétrica asignada a este registro de asistencia")
    private Tipo tipo;
}
