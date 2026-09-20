package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

/**
 * Patrón Command: encapsula una acción reversible sobre un candidato.
 */
public interface ComandoCandidato {

    EventoDominio ejecutar();

    EventoDominio deshacer();
}
