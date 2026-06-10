package com.smartCampus.Ms_Carrera.Exception;



public class CarreraNotFoundException extends RuntimeException {

    private final Long idCarrera;

    public CarreraNotFoundException(Long id){
        super("Carrera no encontrada con ID" + id);
        this.idCarrera = id;
    }

    public long getIdCarrera(){
        return idCarrera;
    }


}
