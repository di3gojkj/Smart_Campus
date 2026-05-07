package com.smartcampus.msAsignatura.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "SEMESTRE")
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Semestre")
    private Long idSemestre;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre_semestre;
}
