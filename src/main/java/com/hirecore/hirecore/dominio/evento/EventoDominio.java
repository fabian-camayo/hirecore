package com.hirecore.hirecore.dominio.evento;

import java.time.Instant;

/**
 * Contrato compartido entre Command y Observer.
 * El comando lo produce al ejecutarse o deshacerse; el bus de eventos lo consume.
 * No pertenece a ninguno de los dos patrones: es información compartida.
 */
public interface EventoDominio {

    String candidatoId();

    Instant ocurridoEn();
}
