package com.smartcampus.msAsignatura.exception;

public class AsignaturaNotFoundException extends RuntimeException {

    // Guardamos el ID para poder incluirlo en el log del GlobalExceptionHandler
    private final long id_Asignatura;

    public AsignaturaNotFoundException(Long id){

        super("Asignatura no encontrada con ID" + id);
        this.id_Asignatura = id;
    }

    public long getId_Asignatura(){
        return id_Asignatura;
    }

}
