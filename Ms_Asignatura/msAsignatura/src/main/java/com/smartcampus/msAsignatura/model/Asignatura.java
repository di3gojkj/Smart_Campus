package com.smartcampus.msAsignatura.model;

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
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "asignaturas")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Long id_Asignatura;

    @Column(name = "nombre_asignatura", unique = true, nullable =false, length = 50)
    private String nombre_asignatura;

    @Column(name = "Sigla", unique = true, nullable = false, length = 10)
    private String sigla;

    @Column(name = "ID_ESTADO") 
    private Long idEstado; //Micro servicio de Gestion Estado

    
}
