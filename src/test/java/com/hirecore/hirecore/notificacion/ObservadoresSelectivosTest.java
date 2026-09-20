package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
class ObservadoresSelectivosTest {

    @Test
    void gerenteYNominaIgnoranCambiosTempranos(CapturedOutput output) {
        EstadoCambiado entrevista = new EstadoCambiado("c-50", "APLICADO", "ENTREVISTA", "sofia");

        new NotificarGerente().manejarNotificacion(entrevista);
        new NotificacionNomina().manejarNotificacion(entrevista);

        assertFalse(output.getAll().contains("[Gerente]"));
        assertFalse(output.getAll().contains("[Nómina]"));
    }

    @Test
    void gerenteReaccionaAOfertaYNominaAContratado(CapturedOutput output) {
        new NotificarGerente().manejarNotificacion(
                new EstadoCambiado("c-51", "PRUEBA_TECNICA", "OFERTA", "sofia")
        );
        new NotificacionNomina().manejarNotificacion(
                new EstadoCambiado("c-51", "OFERTA", "CONTRATADO", "sofia")
        );

        assertTrue(output.getAll().contains("[Gerente] Candidato c-51 está en OFERTA"));
        assertTrue(output.getAll().contains("[Nómina] Alta de c-51"));
    }
}
