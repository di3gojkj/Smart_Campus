package com.SCampus.curso_seccion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "secciones")
@Schema(description = "Entidad que representa una sección académica en la base de datos")
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único auto-incremental de la sección", example = "5")
    private Long id;

    @NotBlank(message = "El nombre de la sección es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Nombre identificador o código de la sección", example = "Sección A", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    @Schema(description = "Curso académico al cual pertenece de forma estricta esta sección")
    private Curso curso;

    
}