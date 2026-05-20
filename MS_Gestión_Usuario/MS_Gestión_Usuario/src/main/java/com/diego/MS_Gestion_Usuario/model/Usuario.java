package com.diego.MS_Gestion_Usuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false)
    private String clave;

    // Relación lógica hacia el MS Estado
    @Column(name = "id_estado", nullable = false)
    private Long idEstado;

    // Relación Muchos a Muchos interna con Roles (Mapea la tabla intermedia rol_user)
    @ManyToMany(fetch = FetchType.EAGER) //DUDA IA EXPLICAR PROFE PLS
    @JoinTable(
        name = "rol_user",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

}
