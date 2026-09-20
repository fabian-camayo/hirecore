package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

public interface PublicarEventos {

    void publicar(EventoDominio evento);
}
