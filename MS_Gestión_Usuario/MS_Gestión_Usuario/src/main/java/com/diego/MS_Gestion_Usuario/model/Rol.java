package com.diego.MS_Gestion_Usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Schema(description = "Entidad que define los niveles de privilegios (ESTUDIANTE, DOCENTE, ADMIN)")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    @Schema(description = "Identificador único del rol", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idRol;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "Nombre descriptivo del rol", example = "ESTUDIANTE")
    private String nombre;

    @Column(name = "id_estado", nullable = false)
    @Schema(description = "ID del estado de disponibilidad del rol", example = "1")
    private Long idEstado;
}