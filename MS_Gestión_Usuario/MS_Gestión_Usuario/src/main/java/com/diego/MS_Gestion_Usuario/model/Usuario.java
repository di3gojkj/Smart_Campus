package com.diego.MS_Gestion_Usuario.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad principal que representa un usuario registrado en la base de datos")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    @Schema(description = "Identificador único del usuario autogenerado", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 12)
    @Schema(description = "RUT del usuario con guión y dígito verificador", example = "12345678-9")
    private String rut;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre completo del usuario", example = "Diego")
    private String nombre;

    @Column(nullable = false, length = 100)
    @Schema(description = "Apellido paterno/materno del usuario", example = "Rivas")
    private String apellido;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Correo electrónico institucional", example = "diego.rivas@duocuc.cl")
    private String correo;

    @Column(nullable = false)
    @Schema(description = "Contraseña encriptada en BCrypt (Nunca viaja en texto plano)")
    private String clave;

    @Column(name = "id_estado", nullable = false)
    @Schema(description = "ID referencial para el MS de Estados (Ej: 1=Activo, 2=Inactivo)", example = "1")
    private Long idEstado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rol_user",
        joinColumns = @JoinColumn(name = "id_user"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    @Schema(description = "Listado de roles (niveles de acceso) asignados al usuario")
    private Set<Rol> roles = new HashSet<>();
}