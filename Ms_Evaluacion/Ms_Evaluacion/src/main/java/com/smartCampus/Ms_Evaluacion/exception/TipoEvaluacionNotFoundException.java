package com.smartCampus.Ms_Evaluacion.exception;

public class TipoEvaluacionNotFoundException extends RuntimeException {

    // Guardamos el ID para poder incluirlo en el log del GlobalExceptionHandler

    private final long idTipoEval;

    public TipoEvaluacionNotFoundException(Long id){
        super("tipo Evaluacion no encontrada con ID " + id);
        this.idTipoEval = id;
    }

    public long getIdTipoEval(){
        return idTipoEval;
    }

}
