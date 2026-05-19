package com.smartcampus.msAsignatura.model;

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
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Long id_Asignatura;

    @Column(name = "nombre", unique = true, nullable =false, length = 100)
    @NotBlank(message = "El nombre de la asignatura no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Column(name = "Sigla", unique = true, nullable = false, length = 10)
    @NotBlank(message = "La sigla académica es obligatoria")
    @Size(max = 10, message = "La sigla no puede superar los 10 caracteres")
    private String sigla;

    @Column(name = "ID_ESTADO", nullable = false)
    @NotNull(message = "El ID de estado es mandatorio") 
    private Long idEstado; //Micro servicio de Gestion Estado

    
}
