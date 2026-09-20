package com.hirecore.hirecore.dominio.excepcion;

public class HistorialVacioException extends RuntimeException {

    public HistorialVacioException() {
        super("No hay comandos en el historial para deshacer");
    }
}
