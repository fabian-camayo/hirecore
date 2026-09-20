package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.CambioRevertido;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificarReclutador implements AvisarObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificarReclutador.class);

    @Override
    public void manejarNotificacion(EventoDominio evento) {
        switch (evento) {
            case EstadoCambiado cambiado -> log.info(
                    "[Reclutador] Candidato {} pasó de {} a {} (autor: {})",
                    cambiado.candidatoId(),
                    cambiado.estadoAnterior(),
                    cambiado.nuevoEstado(),
                    cambiado.autor()
            );
            case CambioRevertido revertido -> log.info(
                    "[Reclutador] Se deshizo el cambio de {}. Estado restaurado: {}",
                    revertido.candidatoId(),
                    revertido.estadoRestaurado()
            );
            default -> log.info("[Reclutador] Evento recibido para {}", evento.candidatoId());
        }
    }
}
