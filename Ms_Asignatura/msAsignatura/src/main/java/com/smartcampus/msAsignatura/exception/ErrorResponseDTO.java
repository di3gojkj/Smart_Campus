package com.smartcampus.msAsignatura.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime timestamp;

    /** Codigo HTTP numerico (404, 400, 409, 500, etc.) */
    private int status;

    /** Descripcion textual del estado HTTP ("Not Found", "Bad Request", etc.) */
    private String error;

    /** Mensaje descriptivo del problema, orientado al desarrollador consumidor */
    private String mensaje;

    /** Path de la URL que genero el error (util para depuracion) */
    private String path;

    /**esto es para acumular los NotBlank, null, positive, etc */
    private List<String> detalles; 

}
