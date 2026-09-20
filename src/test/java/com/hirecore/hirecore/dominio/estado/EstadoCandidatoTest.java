package com.hirecore.hirecore.dominio.estado;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.excepcion.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadoCandidatoTest {

    private final EstadoFactory factory = new EstadoFactory();

    @Test
    void aplicadoSoloPermiteEntrevistaORechazo() {
        EstadoCandidato aplicado = factory.crear(NombreEstado.APLICADO);

        assertTrue(aplicado.puedeTransicionar(factory.crear(NombreEstado.ENTREVISTA)));
        assertTrue(aplicado.puedeTransicionar(factory.crear(NombreEstado.RECHAZADO)));
        assertFalse(aplicado.puedeTransicionar(factory.crear(NombreEstado.CONTRATADO)));
        assertFalse(aplicado.puedeTransicionar(factory.crear(NombreEstado.OFERTA)));
    }

    @Test
    void noSePuedeSaltarDeAplicadoAContratado() {
        Candidato candidato = new Candidato("c-10", factory.crear(NombreEstado.APLICADO));

        TransicionEstadoInvalidaException error = assertThrows(
                TransicionEstadoInvalidaException.class,
                () -> factory.crear(NombreEstado.CONTRATADO).avanzar(candidato)
        );

        assertEquals(NombreEstado.APLICADO.name(), error.getEstadoActual());
        assertEquals(NombreEstado.CONTRATADO.name(), error.getEstadoDestino());
        assertEquals(NombreEstado.APLICADO.name(), candidato.obtenerEstado().nombre());
    }

    @Test
    void laRutaValidaLlegaHastaContratado() {
        Candidato candidato = new Candidato("c-11", factory.crear(NombreEstado.APLICADO));

        factory.crear(NombreEstado.ENTREVISTA).avanzar(candidato);
        factory.crear(NombreEstado.PRUEBA_TECNICA).avanzar(candidato);
        factory.crear(NombreEstado.OFERTA).avanzar(candidato);
        factory.crear(NombreEstado.CONTRATADO).avanzar(candidato);

        assertEquals(NombreEstado.CONTRATADO.name(), candidato.obtenerEstado().nombre());
    }

    @Test
    void estadosTerminalesNoPermitenSeguir() {
        assertFalse(factory.crear(NombreEstado.CONTRATADO).puedeTransicionar(factory.crear(NombreEstado.RECHAZADO)));
        assertFalse(factory.crear(NombreEstado.RECHAZADO).puedeTransicionar(factory.crear(NombreEstado.APLICADO)));
    }
}
