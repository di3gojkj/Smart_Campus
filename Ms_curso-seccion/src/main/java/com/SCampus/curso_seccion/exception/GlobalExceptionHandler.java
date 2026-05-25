package com.SCampus.curso_seccion.exception;

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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponseDTO construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return new ErrorResponseDTO(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, path, detalles);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleaValidateErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        logger.warn("Validación fallida en entorno Curso-Sección - Path: {}", request.getRequestURI());
        return ResponseEntity.badRequest().body(construirError(HttpStatus.BAD_REQUEST, "Errores de validación en los datos de entrada.", request.getRequestURI(), detalles));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJsonMalformado(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.error("JSON ilegible recibido en el endpoint académico: {}", request.getRequestURI());
        return ResponseEntity.badRequest().body(construirError(HttpStatus.BAD_REQUEST, "Formato JSON inválido en el cuerpo de la petición.", request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> manejarTipoIncompatible(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.warn("Conversión de tipo fallida en URL: {}", request.getRequestURI());
        return ResponseEntity.badRequest().body(construirError(HttpStatus.BAD_REQUEST, "El parámetro proporcionado en la URL no tiene un formato válido.", request.getRequestURI(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handlerRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("Inconsistencia lógica de negocio - Path: {} | Mensaje: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(construirError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null));
    }
}
