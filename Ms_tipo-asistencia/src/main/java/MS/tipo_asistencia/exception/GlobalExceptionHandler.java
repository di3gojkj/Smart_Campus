package MS.tipo_asistencia.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest; 

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Auxiliar para construir la respuesta estructurada usando tu clase ErrorResponse
    private ErrorResponse construirError(HttpStatus status, String mensaje, String path, List<String> detalles) {
        return new ErrorResponse(
            LocalDateTime.now(), 
            status.value(), 
            status.getReasonPhrase(), 
            mensaje, 
            path, 
            detalles
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // CORREGIDO: Cambiado HttpIdServletRequest por HttpServletRequest
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("Campo '%s': %s", error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(
            construirError(HttpStatus.BAD_REQUEST, "Errores de validación en los datos de entrada.", request.getRequestURI(), detalles)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    // CORREGIDO: Cambiado HttpIdServletRequest por HttpServletRequest
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            construirError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    // CORREGIDO: Cambiado HttpIdServletRequest por HttpServletRequest
    public ResponseEntity<ErrorResponse> manejarJsonMalformado(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
            construirError(HttpStatus.BAD_REQUEST, "Formato JSON inválido en el cuerpo de la petición.", request.getRequestURI(), null)
        );
    }
}
