package com.smartCampus.Ms_Evaluacion.exception;

import jakarta.servlet.http.HttpServletRequest;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    

    @ExceptionHandler(EvaluacionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEvaluacionNotFound(EvaluacionNotFoundException ex, 
        HttpServletRequest req){
        logger.warn("Evaluacion no encontrada: {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(),
        req.getRequestURI(), null));
    }

    @ExceptionHandler(EvaluacionConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleEvaluacionConflict(EvaluacionConflictException ex, HttpServletRequest req) {
        logger.warn("Conflicto en Evaluacion: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null));
    } 

    
    @ExceptionHandler(TipoEvaluacionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleTipoEvaluacionNotFound(TipoEvaluacionNotFoundException ex,
         HttpServletRequest req) {
        logger.warn("Tipo Evaluacion no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(),
        req.getRequestURI(), null));
    }

    @ExceptionHandler(TipoEvaluacionConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleTipoEvaluacionConflict(TipoEvaluacionConflictException ex, HttpServletRequest req) {
        logger.warn("Conflicto en Evaluacion: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null));
    } 

    /*Atrapa errores de validacion (@NotBlank, @NotNull, @Size)*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO>handleValidaciones(MethodArgumentNotValidException ex,
        HttpServletRequest req){
            List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(construirError(HttpStatus.BAD_REQUEST,
                     "Error de validacion en los datos enviados", req.getRequestURI(),detalles));
        }
        
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest req) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null));
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

