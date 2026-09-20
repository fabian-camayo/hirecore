package com.hirecore.hirecore.dominio.estado;

/**
 * Nombres canónicos de los estados del proceso de selección.
 */
public enum NombreEstado {
    APLICADO,
    ENTREVISTA,
    REFERENCIA,
    PRUEBA_TECNICA,
    OFERTA,
    CONTRATADO,
    RECHAZADO;

    public static NombreEstado desde(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El nombre del estado no puede estar vacío");
        }
        String normalizado = valor.trim()
                .toUpperCase()
                .replace(' ', '_')
                .replace('-', '_');
        return switch (normalizado) {
            case "APLICADO" -> APLICADO;
            case "ENTREVISTA" -> ENTREVISTA;
            case "REFERENCIA", "REFERENCIAS" -> REFERENCIA;
            case "PRUEBA_TECNICA", "PRUEBATECNICA" -> PRUEBA_TECNICA;
            case "OFERTA" -> OFERTA;
            case "CONTRATADO" -> CONTRATADO;
            case "RECHAZADO" -> RECHAZADO;
            default -> throw new IllegalArgumentException("Estado desconocido: " + valor);
        };
    }
}
