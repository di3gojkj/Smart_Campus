package com.cur_eva.model;

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
public class CursoEvaluacion {
    @Id //Indica que es una llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//indica que su valor sera autoincrementable
    @Column(name = "id_cursoEvaluacion")
    private Long idCursoEvaluacion;

    @Column(nullable = false, unique = true, length = 50)//Indica que el valor no puede ser nulo y que no pueden existir dos valores con el mismo nombre
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 50)//Indica que el valor no puede ser nulo y que no pueden existir dos valores con el mismo nombre
    private String fCreacion;

    @Column(nullable = false, unique = true, length = 50)//Indica que el valor no puede ser nulo y que no pueden existir dos valores con el mismo nombre
    private String fCierre;

    @Column(nullable = false, unique = true, length = 50)//Indica que el valor no puede ser nulo y que no pueden existir dos valores con el mismo nombre
    private String fApertura;
}