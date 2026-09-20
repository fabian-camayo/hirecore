package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.estado.NombreEstado;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificacionNomina implements AvisarObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificacionNomina.class);

    @Override
    public void manejarNotificacion(EventoDominio evento) {
        if (evento instanceof EstadoCambiado cambiado
                && NombreEstado.CONTRATADO.name().equals(cambiado.nuevoEstado())) {
            log.info(
                    "[Nómina] Alta de {} para iniciar contrato y pagos",
                    cambiado.candidatoId()
            );
        }
    }
}
