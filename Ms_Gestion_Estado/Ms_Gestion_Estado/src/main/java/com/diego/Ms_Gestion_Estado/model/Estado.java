package com.diego.Ms_Gestion_Estado.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa los estados disponibles en el sistema (Ej: ACTIVO, INACTIVO)")
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(description = "Identificador único del estado autogenerado", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idEstado;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre descriptivo del estado en mayúsculas", example = "ACTIVO")
    private String nombre;
}
