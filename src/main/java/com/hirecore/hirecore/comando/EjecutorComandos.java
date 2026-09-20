package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.evento.EventoDominio;
import com.hirecore.hirecore.notificacion.PublicarEventos;
import org.springframework.stereotype.Component;

@Component
public class EjecutorComandos {

    private final HistorialComandos historial;
    private final PublicarEventos publicador;

    public EjecutorComandos(HistorialComandos historial, PublicarEventos publicador) {
        this.historial = historial;
        this.publicador = publicador;
    }

    public EventoDominio ejecutar(ComandoCandidato comando) {
        EventoDominio evento = comando.ejecutar();
        historial.registrar(comando);
        publicador.publicar(evento);
        return evento;
    }

    public EventoDominio deshacer() {
        ComandoCandidato comando = historial.deshacer();
        EventoDominio evento = comando.deshacer();
        publicador.publicar(evento);
        return evento;
    }
}
