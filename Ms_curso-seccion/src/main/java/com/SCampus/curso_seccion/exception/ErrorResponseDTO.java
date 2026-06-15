package com.SCampus.curso_seccion.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "ErrorResponse", 
    description = "Estructura estándar utilizada por el microservicio para responder ante excepciones y errores del sistema"
)
public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
        description = "Fecha y hora exacta en la que ocurrió el incidente", 
        example = "2026-06-14 20:30:15"
    )
    private LocalDateTime timestamp;

    @Schema(
        description = "Código numérico del estado HTTP", 
        example = "400"
    )
    private int status;

    @Schema(
        description = "Nombre oficial o texto del estado HTTP correspondiente", 
        example = "Bad Request"
    )
    private String error;

    @Schema(
        description = "Mensaje general aclaratorio sobre el error ocurrido", 
        example = "El cuerpo de la petición contiene datos inválidos"
    )
    private String mensaje;

    @Schema(
        description = "Ruta o URI del endpoint donde se originó el fallo", 
        example = "/api/cursos/guardar"
    )
    private String path;

    @Schema(
        description = "Lista detallada con cada una de las fallas de validación específicas detectadas en los campos del Request", 
        example = "[\"Campo 'fechaCreacion': La fecha debe tener la estructura estricta DD/MM/AA (Valor rechazado: '14-06-2026')\"]"
    )
    private List<String> detalles;
}