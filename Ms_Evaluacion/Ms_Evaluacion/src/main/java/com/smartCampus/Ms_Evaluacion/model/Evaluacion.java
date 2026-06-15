package com.smartCampus.Ms_Evaluacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Evaluacion")
@Tag(name = )
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Evaluacion")
    @Schema(
        descripton = ("Identificador único de la evaluación"),
        example = "1",
        mediaType
    )
    private Long id_Evaluacion;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_Tipo_Evaluacion", nullable = false)
    private TipoEvaluacion tipoEvaluacion;

}
