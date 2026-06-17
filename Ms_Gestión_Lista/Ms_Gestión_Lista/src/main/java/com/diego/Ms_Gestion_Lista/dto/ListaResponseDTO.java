package com.diego.Ms_Gestion_Lista.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta que devuelve los datos de la inscripción")
public class ListaResponseDTO {
    
    @Schema(description = "ID autogenerado de la lista", example = "1")
    private Long idLista;
    
    @Schema(description = "ID del usuario (Estudiante)", example = "10")
    private Long idUser;
    
    @Schema(description = "ID del curso inscrito", example = "5")
    private Long idCurso;
    
    @Schema(description = "Fecha exacta en la que se generó la inscripción", example = "2026-06-17T10:30:00")
    private LocalDateTime fechaCreacion;
}