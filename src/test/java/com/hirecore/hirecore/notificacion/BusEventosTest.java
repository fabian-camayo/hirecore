package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusEventosTest {

    @Test
    void reparteElEventoATodosLosObservadoresRegistrados() {
        List<EventoDominio> recibidosA = new ArrayList<>();
        List<EventoDominio> recibidosB = new ArrayList<>();
        BusEventos bus = new BusEventos(List.of(recibidosA::add, recibidosB::add));
        EstadoCambiado evento = new EstadoCambiado("c-40", "APLICADO", "ENTREVISTA", "sofia");

        bus.publicar(evento);

        assertEquals(List.of(evento), recibidosA);
        assertEquals(List.of(evento), recibidosB);
    }

    @Test
    void desregistrarDejaDeNotificarAEseObservador() {
        List<EventoDominio> recibidos = new ArrayList<>();
        AvisarObserver observador = recibidos::add;
        BusEventos bus = new BusEventos(List.of());
        bus.registrar(observador);

        bus.desregistrar(observador);
        bus.publicar(new EstadoCambiado("c-41", "APLICADO", "ENTREVISTA", "sofia"));

        assertTrue(recibidos.isEmpty());
    }
}
