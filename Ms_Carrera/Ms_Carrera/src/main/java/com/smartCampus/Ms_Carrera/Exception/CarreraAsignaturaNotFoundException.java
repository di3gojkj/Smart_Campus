package com.smartCampus.Ms_Carrera.Exception;



public class CarreraAsignaturaNotFoundException extends RuntimeException {

    private final Long idCarreraAsignatura;

    public CarreraAsignaturaNotFoundException(Long id){
        super("Carrera Asignatura no encontrada con ID" + id);
        this.idCarreraAsignatura = id;
    }

    public long getIdCarreraAsignatura(){
        return idCarreraAsignatura;
    }


}
