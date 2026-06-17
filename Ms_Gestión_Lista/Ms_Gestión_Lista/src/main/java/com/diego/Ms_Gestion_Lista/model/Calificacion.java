package com.diego.Ms_Gestion_Lista.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calificaciones")
@Schema(description = "Entidad que representa una nota obtenida por un alumno en una lista")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calif")
    @Schema(description = "ID único de la calificación", example = "1")
    private Long idCalificacion;

    @Column(nullable = false, precision = 3, scale = 1)
    @Schema(description = "Nota obtenida en formato decimal", example = "6.5")
    private BigDecimal nota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lista", nullable = false)
    @Schema(description = "Referencia a la lista/inscripción del alumno", hidden = true)
    private Lista lista;

    @Column(name = "id_cureva", nullable = false)
    @Schema(description = "ID de la evaluación del curso asignada", example = "2")
    private Long idCurEva;
}

