package com.diego.MS_Gestion_Usuario.exception;

public class UsuarioNotFoundException extends RuntimeException {
    private final Long usuarioId;

    public UsuarioNotFoundException(Long id) {
        super("Usuario no localizado con el ID: " + id);
        this.usuarioId = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

}
