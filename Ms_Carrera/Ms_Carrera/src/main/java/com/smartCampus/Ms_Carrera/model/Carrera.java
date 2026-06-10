package com.smartCampus.Ms_Carrera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Carrera")

@Schema(description = "Entidad que representa una carrera Academica")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Carrera")
    @Schema(description = "Id unico generado por la BD",
        example = "1",
        accessMode =Schema.AccessMode.READ_ONLY
    )
    private Long idCarrera;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(
        description = "Nombre de la carrera",
        example = "Ingenieria en Informatica"
    )
    private String nombre;

    @NotBlank(message = "La sigla es obligatoria")
    @Size(min = 2, max = 10, message = "La sigla debe tener entre 2 y 10 caracteres")
    @Column(name = "sigla", nullable = false, unique = true, length = 10)
    @Schema(
        description = "Sigla de carrera",
        example = "INF-001"
    )
    private String sigla;


    @NotNull(message = "El ID del estado es obligatorio")
    @Min(value = 1, message = "El ID del estado debe ser un número positivo")
    @Column(name = "id_estado", nullable = false)
    @Schema(
        description = "Id del micro servicio de Gestion Estado ",
        example = "1l"
    )
    private Long idEstado;
}
