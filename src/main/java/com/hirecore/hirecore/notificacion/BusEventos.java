package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementación concreta de {@link PublicarEventos} y sujeto del Observer:
 * mantiene la lista de interesados y les reparte cada evento.
 */
@Component
public class BusEventos implements PublicarEventos {

    private final List<AvisarObserver> observadores = new CopyOnWriteArrayList<>();

    public BusEventos(List<AvisarObserver> observadores) {
        if (observadores != null) {
            this.observadores.addAll(observadores);
        }
    }

    public void registrar(AvisarObserver observador) {
        observadores.add(observador);
    }

    public void desregistrar(AvisarObserver observador) {
        observadores.remove(observador);
    }

    @Override
    public void publicar(EventoDominio evento) {
        observadores.forEach(observador -> observador.manejarNotificacion(evento));
    }
}
