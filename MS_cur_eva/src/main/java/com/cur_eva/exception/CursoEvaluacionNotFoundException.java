package com.cur_eva.exception;

// CORREGIDO: Ahora hereda de RuntimeException para ser una excepción válida en Java/Spring
public class CursoEvaluacionNotFoundException extends RuntimeException {
    
    private final Long cursoEvaluacionId;

    public CursoEvaluacionNotFoundException(Long id, String mensaje) {
        // CORREGIDO: Enviamos el mensaje al constructor padre para que ex.getMessage() no devuelva nulo
        super(mensaje);
        this.cursoEvaluacionId = id;
    }
    
    public Long getCursoEvaluacionId() {
        return cursoEvaluacionId;
    }
}

