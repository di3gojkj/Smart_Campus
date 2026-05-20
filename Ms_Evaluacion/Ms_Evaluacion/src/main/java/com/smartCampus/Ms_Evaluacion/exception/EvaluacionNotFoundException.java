package com.smartCampus.Ms_Evaluacion.exception;

public class EvaluacionNotFoundException extends RuntimeException {

    // Guardamos el ID para poder incluirlo en el log del GlobalExceptionHandler

    private final long id_Evaluacion;

    public EvaluacionNotFoundException(Long id){
        super("Evaluacion no encontrada con ID" + id);
        this.id_Evaluacion = id;
    }

    public long getId_Evaluacion(){
        return id_Evaluacion;
    }

}
