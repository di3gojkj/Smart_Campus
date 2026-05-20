package com.diego.Ms_Gestion_Lista.exception;

public class RegistroNotFoundException extends RuntimeException {
    public RegistroNotFoundException(String mensaje) {
        super(mensaje);
    }
}
