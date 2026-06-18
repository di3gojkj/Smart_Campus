package com.smartCampus.Ms_Evaluacion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "evaluacion")
@Schema(description = "Entidad que representa una Evaluacion Academica")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    @Schema(
        description = "Id unico generado por la BD",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idEvaluacion;

    @NotBlank(message = "El nombre de la evaluacion es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    @Schema(
        description = "Nombre de la evaluacion",
        example = "Certamen 1"
    )
    private String nombre;

    @NotNull(message = "El porcentaje es obligatorio")
    @Column(name = "porcentaje", nullable = false)
    @Schema(
        description = "Porcentaje de ponderacion de la evaluacion",
        example = "30.0"
    )
    private Double porcentaje;

    @NotNull(message = "El tipo de evaluacion es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_evaluacion", nullable = false)
    @Schema(description = "Tipo de evaluacion asociado")
    private TipoEvaluacion tipoEvaluacion;
}
