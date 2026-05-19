package com.smartcampus.msAsignatura.model;

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
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Semestre")
    private Long idSemestre;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    @NotBlank(message = "El nombre del semestre es obligatorio (Ej: 2026-1)")
    @Size(min = 6, max = 50, message = "El nombre del semestre debe tener entre 6 a 50 caracteres")
    private String nombre;

    @Column(name = "ID_ESTADO", nullable = false)
    @NotNull(message = "El ID de estado es mandatorio") 
    private Long idEstado; //Micro servicio de Gestion Estado
}
