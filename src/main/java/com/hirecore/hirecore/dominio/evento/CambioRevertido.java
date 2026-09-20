package com.hirecore.hirecore.dominio.evento;

import java.time.Instant;
import java.util.Objects;

/**
 * Evento publicado cuando se deshace un cambio de estado.
 */
public record CambioRevertido(
        String candidatoId,
        String estadoRestaurado,
        String autor,
        Instant ocurridoEn
) implements EventoDominio {

    public CambioRevertido {
        Objects.requireNonNull(candidatoId, "candidatoId");
        Objects.requireNonNull(estadoRestaurado, "estadoRestaurado");
        Objects.requireNonNull(autor, "autor");
        Objects.requireNonNull(ocurridoEn, "ocurridoEn");
    }

    public CambioRevertido(String candidatoId, String estadoRestaurado, String autor) {
        this(candidatoId, estadoRestaurado, autor, Instant.now());
    }
}
