package com.smartcampus.msAsignatura.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
@Table(name = "asignaturas")
@Schema(description = "Entidad que representa una Asignatura Academica")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    @Schema(
        description = "Id único generado por la BD", 
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY)
    private Long idAsignatura; 

    @NotBlank(message = "El nombre de la asignatura no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    @Schema(
        description = "Nombre de la asignatura", 
        example = "Estructuras de Datos")
    private String nombre;

    @NotBlank(message = "La sigla académica es obligatoria")
    @Size(max = 10, message = "La sigla no puede superar los 10 caracteres")
    @Column(name = "sigla", unique = true, nullable = false, length = 10) // CORREGIDO: minúsculas
    @Schema(
        description = "Sigla academica de la asignatura",
        example = "INF-230")
    private String sigla;

    @NotNull(message = "El ID de estado es mandatorio")
    @Column(name = "id_estado", nullable = false)
    @Schema(
        description = "Id del microservicio de Gestión Estado",
        example = "1")
    private Long idEstado;

    
}
