package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.estado.EstadoFactory;
import com.hirecore.hirecore.dominio.estado.NombreEstado;
import com.hirecore.hirecore.dominio.evento.CambioRevertido;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import com.hirecore.hirecore.dominio.excepcion.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CambiarEstadoCommandTest {

    private EstadoFactory factory;
    private Candidato candidato;

    @BeforeEach
    void setUp() {
        factory = new EstadoFactory();
        candidato = new Candidato("c-20", factory.crear(NombreEstado.APLICADO));
    }

    @Test
    void ejecutarCambiaElEstadoYProduceEvento() {
        CambiarEstadoCommand comando = new CambiarEstadoCommand(candidato, "ENTREVISTA", "sofia", factory);

        EventoDominio evento = comando.ejecutar();

        assertEquals(NombreEstado.ENTREVISTA.name(), candidato.obtenerEstado().nombre());
        EstadoCambiado cambiado = assertInstanceOf(EstadoCambiado.class, evento);
        assertEquals("c-20", cambiado.candidatoId());
        assertEquals(NombreEstado.APLICADO.name(), cambiado.estadoAnterior());
        assertEquals(NombreEstado.ENTREVISTA.name(), cambiado.nuevoEstado());
        assertEquals("sofia", cambiado.autor());
    }

    @Test
    void deshacerRestauraElEstadoAnterior() {
        CambiarEstadoCommand comando = new CambiarEstadoCommand(candidato, "ENTREVISTA", "sofia", factory);
        comando.ejecutar();

        EventoDominio evento = comando.deshacer();

        assertEquals(NombreEstado.APLICADO.name(), candidato.obtenerEstado().nombre());
        CambioRevertido revertido = assertInstanceOf(CambioRevertido.class, evento);
        assertEquals(NombreEstado.APLICADO.name(), revertido.estadoRestaurado());
    }

    @Test
    void ejecutarRespetaLasReglasDelEstado() {
        CambiarEstadoCommand comando = new CambiarEstadoCommand(candidato, "CONTRATADO", "sofia", factory);

        assertThrows(TransicionEstadoInvalidaException.class, comando::ejecutar);
        assertEquals(NombreEstado.APLICADO.name(), candidato.obtenerEstado().nombre());
    }
}
