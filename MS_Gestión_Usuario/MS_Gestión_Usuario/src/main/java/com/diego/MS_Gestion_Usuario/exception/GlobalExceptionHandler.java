package com.diego.MS_Gestion_Usuario.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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

        logger.warn("Validación fallida en {} {} - Errores: {}", request.getMethod(), request.getRequestURI(), erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "Los datos enviados contienen errores de validación.", request.getRequestURI(), erroresCampos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("JSON inválido o malformado en la petición - Path: {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "El cuerpo de la petición tiene un formato JSON inválido o tipos de datos incorrectos.", request.getRequestURI(), null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.warn("Tipo de parámetro incorrecto en la URL - Path: {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "Parámetro de URL con formato o tipo inválido.", request.getRequestURI(), null));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logger.warn("Método HTTP no soportado - Método: {} | Path: {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(construirError(HttpStatus.METHOD_NOT_ALLOWED, "El método HTTP utilizado no está permitido para esta ruta.", request.getRequestURI(), null));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarConflictosNegocio(RuntimeException ex, HttpServletRequest request) {
        logger.error("error de comunicación - Path: {} | Mensaje: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(construirError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null));
    }
}
