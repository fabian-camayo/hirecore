package com.hirecore.hirecore.dominio.excepcion;

/**
 * Se lanza cuando el estado actual no permite pasar al estado destino.
 */
public class TransicionEstadoInvalidaException extends RuntimeException {

    private final String estadoActual;
    private final String estadoDestino;

    public TransicionEstadoInvalidaException(String estadoActual, String estadoDestino) {
        super("No se puede transicionar de '%s' a '%s'".formatted(estadoActual, estadoDestino));
        this.estadoActual = estadoActual;
        this.estadoDestino = estadoDestino;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }
}
