package com.diego.Ms_Gestion_Lista.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListaRequestDTO {
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUser;

    @NotNull(message = "El ID de curso es obligatorio")
    private Long idCurso;
}
