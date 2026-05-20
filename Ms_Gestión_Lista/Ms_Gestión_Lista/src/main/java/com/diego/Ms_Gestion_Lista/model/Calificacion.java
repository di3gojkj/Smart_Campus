package com.diego.Ms_Gestion_Lista.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calificaciones")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calif")
    private Long idCalificacion;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal nota;

    // Relación física interna: Múltiples notas pertenecen a un registro de lista/inscripción
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lista", nullable = false)
    private Lista lista;

    // Llave foránea lógica hacia la tabla de evaluaciones del curso (id_cur_eva)
    @Column(name = "id_cureva", nullable = false)
    private Long idCurEva;

}
