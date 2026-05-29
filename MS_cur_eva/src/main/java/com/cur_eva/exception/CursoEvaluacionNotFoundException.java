package com.cur_eva.exception;

public class CursoEvaluacionNotFoundException {
    private final Long cursoEvaluacionId;

    public CursoEvaluacionNotFoundException(Long id) {
       // super("Curso Evaluacion no encontrado con el ID: " + id)
        this.cursoEvaluacionId = id;
        
    }
    
    public Long getCursoEvaluacionId() {
        return cursoEvaluacionId;
    }


   

 
}
