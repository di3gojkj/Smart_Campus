package Ms.confi.exception;

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
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponseDTO construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return new ErrorResponseDTO(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensaje, path, detalles);
    }

    // Atrapa errores de @Valid (ejemplo: correo vacío o sin formato @)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> erroresCampos = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        logger.warn("Intento de Login fallido por validación en {} - Errores: {}", request.getRequestURI(), erroresCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "Las credenciales enviadas no cumplen el formato requerido", request.getRequestURI(), erroresCampos));
    }

    // Atrapa errores cuando mandan un JSON mal escrito en Postman
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.error("JSON Malformado recibido en el Path: {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(construirError(HttpStatus.BAD_REQUEST, "El cuerpo de la petición tiene un formato JSON inválido", request.getRequestURI(), null));
    }

    // Atrapa errores de método (ej: intentar hacer un GET a un endpoint que es POST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logger.warn("Método HTTP no soportado - Path: {}", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(construirError(HttpStatus.METHOD_NOT_ALLOWED, "El método HTTP utilizado no está permitido para esta ruta de autenticación.", request.getRequestURI(), null));
    }

    // Atrapa nuestras excepciones de negocio (ej: "Credenciales incorrectas" del AuthService)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> manejarErroresDeAutenticacion(RuntimeException ex, HttpServletRequest request) {
        logger.error("Fallo de Seguridad o Red - Path: {} | Mensaje: {}", request.getRequestURI(), ex.getMessage());
        
        // Retornamos 401 Unauthorized porque estamos en el contexto de Login
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(construirError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI(), null));
    }
}
