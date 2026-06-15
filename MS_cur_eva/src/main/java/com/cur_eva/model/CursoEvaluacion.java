package com.cur_eva.model;

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

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estados")
@Schema(
    name = "CursoEvaluacionEntity", 
    description = "Entidad de persistencia que representa la tabla 'estados' en la base de datos MySQL"
)
public class CursoEvaluacion {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cursoEvaluacion")
    @Schema(
        description = "Clave primaria autoincrementable de la tabla estados", 
        example = "1"
    )
    private Long idCursoEvaluacion;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(
        description = "Nombre único asignado al estado de la evaluación", 
        example = "ACTIVO", 
        maxLength = 50
    )
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 50)
    @Schema(
        description = "Fecha de creación del registro almacenada en formato de texto", 
        example = "2026-06-15", 
        maxLength = 50
    )
    private String fCreacion;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(
        description = "Fecha límite registrada para el cierre de la evaluación", 
        example = "2026-07-20", 
        maxLength = 50
    )
    private String fCierre;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(
        description = "Fecha programada para la apertura pública de la evaluación", 
        example = "2026-06-20", 
        maxLength = 50
    )
    private String fApertura;
}