package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

/**
 * Promesa de publicación (DIP). El ejecutor solo conoce esta interfaz,
 * nunca el mecanismo concreto que reparte el evento.
 */
public interface PublicarEventos {

    void publicar(EventoDominio evento);
}
