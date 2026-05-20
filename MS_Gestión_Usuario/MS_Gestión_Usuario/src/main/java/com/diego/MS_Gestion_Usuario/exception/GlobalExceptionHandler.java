package com.diego.MS_Gestion_Usuario.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponseDTO construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return new ErrorResponseDTO(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, path, detalles);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> manejarUsuarioNoEncontrado(UsuarioNotFoundException ex, HttpServletRequest request) {
        logger.warn("Excepción: Usuario No Encontrado - ID: {} | Path: {}", ex.getUsuarioId(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(construirError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> erroresCampos = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Campo '%s': %s (Valor recibido: '%s')", error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
            .collect(Collectors.toList());

        logger.warn("Validación fallida en {} - Errores: {}", request.getRequestURI(), erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "Errores de validación en los campos enviados.", request.getRequestURI(), erroresCampos));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarConflictosNegocio(RuntimeException ex, HttpServletRequest request) {
        logger.error("Violación de comunicación - Path: {} | Mensaje: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null));
    }

}
