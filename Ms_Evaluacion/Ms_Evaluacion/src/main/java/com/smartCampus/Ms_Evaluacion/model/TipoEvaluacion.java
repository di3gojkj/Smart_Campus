package com.smartCampus.Ms_Evaluacion.model;

import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tipo_evaluacion")
public class TipoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Tipo_Evaluacion")
    private Long id_Tipo_Evaluacion;

    @Column(name = "nombre_tipo", unique = true, nullable = false, length = 50)
    private String nombreTipo; 

}
