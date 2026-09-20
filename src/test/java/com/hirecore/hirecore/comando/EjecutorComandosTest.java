package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.estado.EstadoFactory;
import com.hirecore.hirecore.dominio.estado.NombreEstado;
import com.hirecore.hirecore.dominio.evento.CambioRevertido;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import com.hirecore.hirecore.dominio.excepcion.HistorialVacioException;
import com.hirecore.hirecore.notificacion.PublicarEventos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EjecutorComandosTest {

    private EstadoFactory factory;
    private HistorialComandos historial;
    private List<EventoDominio> publicados;
    private EjecutorComandos ejecutor;
    private Candidato candidato;

    @BeforeEach
    void setUp() {
        factory = new EstadoFactory();
        historial = new HistorialComandos();
        publicados = new ArrayList<>();
        PublicarEventos publicadorFalso = publicados::add;
        ejecutor = new EjecutorComandos(historial, publicadorFalso);
        candidato = new Candidato("c-30", factory.crear(NombreEstado.APLICADO));
    }

    @Test
    void ejecutarCorreGuardaYPublica() {
        ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "ENTREVISTA", "sofia", factory));

        assertEquals(NombreEstado.ENTREVISTA.name(), candidato.obtenerEstado().nombre());
        assertEquals(1, historial.tamanio());
        assertEquals(1, publicados.size());
        assertInstanceOf(EstadoCambiado.class, publicados.getFirst());
    }

    @Test
    void deshacerReviertePublicaYVaciaElHistorial() {
        ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "ENTREVISTA", "sofia", factory));

        ejecutor.deshacer();

        assertEquals(NombreEstado.APLICADO.name(), candidato.obtenerEstado().nombre());
        assertTrue(historial.vacio());
        assertEquals(2, publicados.size());
        assertInstanceOf(CambioRevertido.class, publicados.get(1));
    }

    @Test
    void noSePuedeDeshacerSinHistorial() {
        assertThrows(HistorialVacioException.class, ejecutor::deshacer);
    }

    @Test
    void transicionInvalidaNoQuedaEnHistorialNiSePublica() {
        assertThrows(
                RuntimeException.class,
                () -> ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "CONTRATADO", "sofia", factory))
        );

        assertTrue(historial.vacio());
        assertTrue(publicados.isEmpty());
        assertEquals(NombreEstado.APLICADO.name(), candidato.obtenerEstado().nombre());
    }
}
