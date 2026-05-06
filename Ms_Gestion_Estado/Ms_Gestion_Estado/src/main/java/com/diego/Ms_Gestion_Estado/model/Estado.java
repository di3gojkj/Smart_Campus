package com.diego.Ms_Gestion_Estado.model;

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
@Table(name = "estados") //Indica que la clase no es un objeto comun si no que se debe transformar en una tabla
public class Estado {
    @Id //Indica que es una llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//indica que su valor sera autoincrementable
    private Long idEstado;

    @Column(nullable = false, unique = true, length = 50)//Indica que el valor no puede ser nulo y que no pueden existir dos valores con el mismo nombre
    private String nombre;


}
