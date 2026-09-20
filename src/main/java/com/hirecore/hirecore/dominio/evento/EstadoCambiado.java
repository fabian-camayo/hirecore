package com.hirecore.hirecore.dominio.evento;

import java.time.Instant;
import java.util.Objects;

/**
 * Evento publicado cuando un comando cambia el estado de un candidato.
 */
public record EstadoCambiado(
        String candidatoId,
        String estadoAnterior,
        String nuevoEstado,
        String autor,
        Instant ocurridoEn
) implements EventoDominio {

    public EstadoCambiado {
        Objects.requireNonNull(candidatoId, "candidatoId");
        Objects.requireNonNull(estadoAnterior, "estadoAnterior");
        Objects.requireNonNull(nuevoEstado, "nuevoEstado");
        Objects.requireNonNull(autor, "autor");
        Objects.requireNonNull(ocurridoEn, "ocurridoEn");
    }

    public EstadoCambiado(String candidatoId, String estadoAnterior, String nuevoEstado, String autor) {
        this(candidatoId, estadoAnterior, nuevoEstado, autor, Instant.now());
    }
}
