package com.diego.Ms_Gestion_Estado.exception;

public class EstadoNotFoundException extends RuntimeException {
    private final Long estadoId;

    public EstadoNotFoundException(Long id) {
        super("Estado no encontrado con el ID: " + id);
        this.estadoId = id;
    }
    
    public Long getEstadoId() {
        return estadoId;
    }
}
