package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

/**
 * Patrón Observer: interesado que reacciona a un evento de dominio
 * sin que el flujo principal sepa quién es.
 */
public interface AvisarObserver {

    void manejarNotificacion(EventoDominio evento);
}
