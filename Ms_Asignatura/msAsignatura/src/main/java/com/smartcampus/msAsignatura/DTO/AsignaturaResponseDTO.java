package com.smartcampus.msAsignatura.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class AsignaturaResponseDTO {

    private Long id_Asignatura;
    private String nombre;
    private String sigla;
    private Long idEstado; /*Se expone el Id referencial del otro MicroServicio */

    private String nombreEstado; 
    
    @JsonProperty("ACTIVO") /* Fuerza a que en el JSON salga en mayúsculas */ 
    private boolean Activo;

}
