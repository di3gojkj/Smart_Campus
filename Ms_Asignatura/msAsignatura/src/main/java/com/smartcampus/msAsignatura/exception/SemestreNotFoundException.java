package com.smartcampus.msAsignatura.exception;

public class SemestreNotFoundException extends RuntimeException {

    // Guardamos el ID para poder incluirlo en el log del GlobalExceptionHandler
    private final long idSemestre;

    public SemestreNotFoundException(Long id){

        super("Semestre no encontrada con ID" + id);
        this.idSemestre = id;
    }

    public long getIdSemestre(){
        return idSemestre;
    }
}
