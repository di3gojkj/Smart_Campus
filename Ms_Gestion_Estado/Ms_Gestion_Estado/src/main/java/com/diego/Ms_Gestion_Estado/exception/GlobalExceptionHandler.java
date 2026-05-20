package com.diego.Ms_Gestion_Estado.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponseDTO construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return new ErrorResponseDTO(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            mensaje,
            path,
            detalles
        );
    }

    @ExceptionHandler(EstadoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> manejarEstadoNoEncontrado(EstadoNotFoundException ex, HttpServletRequest request) {
        logger.warn("Recurso No Encontrado - ID Estado: {} | Path: {}", ex.getEstadoId(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> erroresCampos = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Campo '%s': %s (Valor rechazado: '%s')", error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .collect(Collectors.toList());

        logger.warn("Validación fallida en {} {} - Errores detectados: {}", request.getMethod(), request.getRequestURI(), erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "Los datos de entrada contienen errores de validación", request.getRequestURI(), erroresCampos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.error("JSON Malformado recibido en el cuerpo de la petición en el Path: {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "El cuerpo de la petición tiene un formato JSON inválido", request.getRequestURI(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarConflictos(RuntimeException ex, HttpServletRequest request) {
        logger.error("Conflicto detectado en la lógica de negocio - Path: {} | Mensaje: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null));
    }
}
