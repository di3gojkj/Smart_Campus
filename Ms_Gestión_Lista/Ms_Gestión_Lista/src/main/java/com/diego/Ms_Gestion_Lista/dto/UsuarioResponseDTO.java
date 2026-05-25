package com.diego.Ms_Gestion_Lista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
}