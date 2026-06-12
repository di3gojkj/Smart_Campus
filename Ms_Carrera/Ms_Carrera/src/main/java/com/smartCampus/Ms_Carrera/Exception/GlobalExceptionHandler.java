package com.smartCampus.Ms_Carrera.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(CarreraAsignaturaNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAsignaturaNotFound(CarreraAsignaturaNotFoundException ex, 
        HttpServletRequest req){
        logger.warn("Carrera Asignatura no encontrada: {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(),
        req.getRequestURI(), null));
    }

    @ExceptionHandler(CarreraAsignaturaConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleAsignaturaConflict(CarreraAsignaturaConflictException ex, HttpServletRequest req) {
        logger.warn("Conflicto en CarreraAsignatura: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null));
    }

    @ExceptionHandler(CarreraNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCarreraNotFound(CarreraNotFoundException ex,
         HttpServletRequest req) {
        logger.warn("Carrera no encontrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(),
        req.getRequestURI(), null));
    }

    @ExceptionHandler(CarreraConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleCarreraConflict(CarreraConflictException ex, HttpServletRequest req) {
        logger.warn("Conflicto en Carrera: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null));
    }

    /*Atrapa errores de validacion (@NotBlank, @NotNull, @Size)*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidaciones(MethodArgumentNotValidException ex,
        HttpServletRequest req){
            List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(construirError(HttpStatus.BAD_REQUEST,
                     "Error de validacion en los datos enviados", req.getRequestURI(),detalles));
        }
    
    
    private ErrorResponseDTO construirError(HttpStatus status, String mensaje,
                                             String path, List<String> detalles) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),  // "Not Found", "Bad Request", etc.
                mensaje,
                path,
                detalles
        );
    }
}
