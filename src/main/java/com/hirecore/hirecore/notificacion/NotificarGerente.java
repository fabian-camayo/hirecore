package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.estado.NombreEstado;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificarGerente implements AvisarObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificarGerente.class);

    @Override
    public void manejarNotificacion(EventoDominio evento) {
        if (evento instanceof EstadoCambiado cambiado
                && esHitoGerencial(cambiado.nuevoEstado())) {
            log.info(
                    "[Gerente] Candidato {} está en {} y requiere revisión",
                    cambiado.candidatoId(),
                    cambiado.nuevoEstado()
            );
        }
    }

    private boolean esHitoGerencial(String estado) {
        return NombreEstado.OFERTA.name().equals(estado)
                || NombreEstado.CONTRATADO.name().equals(estado);
    }
}
