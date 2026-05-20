package com.smartCampus.Ms_Carrera.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Table(name = "Carrera")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Carrera")
    private Long idCarrera;


    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La sigla es obligatoria")
    @Size(min = 2, max = 10, message = "La sigla debe tener entre 2 y 10 caracteres")
    @Column(name = "sigla", nullable = false, unique = true, length = 10)
    private String sigla;


    @NotNull(message = "El ID del estado es obligatorio")
    @Min(value = 1, message = "El ID del estado debe ser un número positivo")
    @Column(name = "id_estado", nullable = false)
    private Long idEstado;
}
