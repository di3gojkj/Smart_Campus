package com.SCampus.curso_seccion.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleaValidateErrors(
        MethodArgumentNotValidException ex
    ){
        Map<String,String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
             errores.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errores);
    }

    //Errores de negocios
    //Categoria no encontrada
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handlerRuntimeException(RuntimeException ex){
        Map<String,String> error = new LinkedHashMap<>();
        error.put("Error", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

}
