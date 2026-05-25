package com.smartCampus.Ms_Evaluacion.model;

import java.util.List;

import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Tipo_Evaluacion")
public class TipoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Tipo_Evaluacion")
    private Long idTipoEval;

    @Column(name = "Nombre_Tipo", unique = true, nullable = false, length = 10)
    private String nombreTipo; //Esto seran los nombres de la evaluacion. EJ: Certamen, Tarea, Control, Tarea

    @OneToMany(mappedBy = "tipoEvaluacion") // "tipoEvaluacion" es el nombre del atributo en tu clase Evaluacion
    private List<Evaluacion> evaluaciones;

}
