package com.smartCampus.Ms_Evaluacion.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tipo_evaluacion")
@Schema(description = "Entidad que representa un Tipo de Evaluacion")
public class TipoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_evaluacion")
    @Schema(
        description = "Id unico generado por la BD",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idTipoEval;

    @NotBlank(message = "El nombre del tipo de evaluacion es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(name = "nombre_tipo", unique = true, nullable = false, length = 50)
    @Schema(
        description = "Nombre del tipo de evaluacion",
        example = "Certamen"
    )
    private String nombreTipo;

    @OneToMany(mappedBy = "tipoEvaluacion")
    @Schema(
        description = "Lista de evaluaciones asociadas a este tipo"
    )
    private List<Evaluacion> evaluaciones;

}
