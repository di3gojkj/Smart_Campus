package com.smartCampus.Ms_Carrera.model;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Carrera_Asignatura")

@Schema(description = "Relacion entre carrera y asignaturas")
public class CarreraAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Carrera_Asignatura")
    @Schema(
        description = "Id de la relacion",
        example = "1",
        accessMode =Schema.AccessMode.READ_ONLY
    )
    private long idCarreraAsignatura;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    @Schema(
        description = "Carrera asociada"
    )
    private Carrera carrera;

    // IDs externos (se mantienen como Long)
    @Column(name = "id_asignatura", nullable = false)
    @Schema(
        description = "ID del microservicio de asignaturas", 
        example = "5"
    )
    private Long idAsignatura;

    @Column(name = "id_semestre", nullable = false)
    @Schema(
        description = "Número del semestre", 
        example = "1"
    )
    private Long idSemestre;

}
