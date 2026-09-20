package com.hirecore.hirecore.notificacion;

import com.hirecore.hirecore.dominio.evento.CambioRevertido;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActualizarPortalCandidato implements AvisarObserver {

    private static final Logger log = LoggerFactory.getLogger(ActualizarPortalCandidato.class);

    @Override
    public void manejarNotificacion(EventoDominio evento) {
        switch (evento) {
            case EstadoCambiado cambiado -> log.info(
                    "[Portal] Publicar estado {} para el candidato {}",
                    cambiado.nuevoEstado(),
                    cambiado.candidatoId()
            );
            case CambioRevertido revertido -> log.info(
                    "[Portal] Restaurar estado {} para el candidato {}",
                    revertido.estadoRestaurado(),
                    revertido.candidatoId()
            );
            default -> log.debug("[Portal] Evento ignorado: {}", evento);
        }
    }
}
