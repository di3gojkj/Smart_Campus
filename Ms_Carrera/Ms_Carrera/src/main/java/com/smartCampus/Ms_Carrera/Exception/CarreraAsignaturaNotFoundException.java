package com.smartCampus.Ms_Carrera.Exception;



public class CarreraAsignaturaNotFoundException extends RuntimeException {

    private final Long idCarreraAsignatura;

    public CarreraAsignaturaNotFoundException(Long id){
        super("Carrera no encontrada por id" + id);
        this.idCarreraAsignatura = id;
    }

    public long getIdCarreraAsignatura(){
        return idCarreraAsignatura;
    }


}
