package com.SCampus.curso_seccion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "CursoEntity", 
    description = "Entidad de persistencia que representa la tabla 'cursos' en la base de datos MySQL"
)
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Clave primaria autoincrementable de la tabla cursos", 
        example = "12"
    )
    private Long id;

    @Column(name = "fecha_creacion", nullable = false, length = 8)
    @Schema(
        description = "Fecha de alta del curso almacenada con longitud exacta de 8 caracteres", 
        example = "14/06/26",
        maxLength = 8
    )
    private String fechaCreacion;
}
