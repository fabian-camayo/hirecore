package com.hirecore.hirecore.dominio.excepcion;

/**
 * Se lanza al intentar deshacer cuando no hay comandos registrados.
 */
public class HistorialVacioException extends RuntimeException {

    public HistorialVacioException() {
        super("No hay comandos en el historial para deshacer");
    }
}
