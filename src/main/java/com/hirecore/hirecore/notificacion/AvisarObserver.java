package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

public interface AvisarObserver {

    void manejarNotificacion(EventoDominio evento);
}
