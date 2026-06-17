package com.diego.Ms_Gestion_Estado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto para registrar un nuevo estado en el sistema")
public class EstadoRequestDTO {

    @NotBlank(message = "El nombre del estado no puede estar vacío")
    @Schema(description = "Nombre del nuevo estado", example = "BLOQUEADO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}