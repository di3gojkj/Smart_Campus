package com.smartcampus.msAsignatura.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
@Table(name = "SEMESTRE")
@Schema(description = "Entidad que representa un Semestre Academico")
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semestre")
    @Schema(
        description = "Id único generado por la BD",
        example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idSemestre;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotBlank(message = "El nombre del semestre es obligatorio (Ej: 2026-1)")
    @Size(min = 6, max = 50, message = "El nombre del semestre debe tener entre 6 a 50 caracteres")
    @Schema(
        description = "Nombre identificador del semestre", example = "2026-1")
    private String nombre;

    @Column(name = "id_estado", nullable = false)
    @NotNull(message = "El ID de estado es mandatorio") 
    @Schema(
        description = "Id del microservicio de Gestión Estado",
        example = "1")
    private Long idEstado; //Micro servicio de Gestion Estado
}
