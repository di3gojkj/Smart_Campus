package com.diego.Ms_Gestion_Lista.model;

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
public class Lista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Long idLista;

    // Llave foránea lógica hacia el MS Usuarios (id_user)
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    // Llave foránea lógica hacia el MS Cursos (id_curso)
    @Column(name = "id_curso", nullable = false)
    private Long idCurso;

    @Column(name = "f_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
