package com.SCampus.curso_seccion.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "Seccion", 
    description = "Representa una sección académica vinculada a un curso específico dentro de la institución"
)
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Identificador único autoincrementable de la sección", 
        example = "5",
        readOnly = true
    )
    private Long id;

    @NotBlank(message = "El nombre de la sección es obligatorio")
    @Column(nullable = false, length = 50)
    @Schema(
        description = "Nombre identificador único de la sección (ej. paralelo, aula o grupo)", 
        example = "Sección A", 
        maxLength = 50,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotNull(message = "El ID de curso asociado es obligatorio")
    @Column(name = "id_curso", nullable = false)
    @Schema(
        description = "Identificador (ID) del curso académico al que pertenece esta sección", 
        example = "12",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idCurso;
}