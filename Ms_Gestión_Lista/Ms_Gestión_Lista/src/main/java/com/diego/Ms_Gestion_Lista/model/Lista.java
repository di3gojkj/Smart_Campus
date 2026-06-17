package com.diego.Ms_Gestion_Lista.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "listas")
@Schema(description = "Entidad que representa la inscripción de un usuario en un curso")
public class Lista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    @Schema(description = "ID único de la lista", example = "1")
    private Long idLista;

    @Column(name = "id_user", nullable = false)
    @Schema(description = "ID del usuario (Estudiante) proveniente del MS Usuarios", example = "10")
    private Long idUser;

    @Column(name = "id_curso", nullable = false)
    @Schema(description = "ID del curso proveniente del MS Cursos", example = "5")
    private Long idCurso;

    @Column(name = "f_creacion", nullable = false)
    @Schema(description = "Fecha y hora en que se creó el registro", example = "2026-06-17 10:00:00")
    private LocalDateTime fechaCreacion;
}