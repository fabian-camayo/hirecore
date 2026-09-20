package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.evento.EventoDominio;

public interface ComandoCandidato {

    EventoDominio ejecutar();

    EventoDominio deshacer();
}
