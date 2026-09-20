package com.hirecore.hirecore.demostracion;

import com.hirecore.hirecore.comando.CambiarEstadoCommand;
import com.hirecore.hirecore.comando.EjecutorComandos;
import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.estado.EstadoFactory;
import com.hirecore.hirecore.dominio.estado.NombreEstado;
import com.hirecore.hirecore.dominio.excepcion.TransicionEstadoInvalidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Recorre el flujo de la arquitectura al arrancar la aplicación:
 * comando → ejecutor → estado → evento → observadores, más un deshacer.
 */
@Component
@ConditionalOnProperty(name = "hirecore.demo.enabled", havingValue = "true", matchIfMissing = true)
public class DemostracionArquitectura implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemostracionArquitectura.class);

    private final EjecutorComandos ejecutor;
    private final EstadoFactory factory;

    public DemostracionArquitectura(EjecutorComandos ejecutor, EstadoFactory factory) {
        this.ejecutor = ejecutor;
        this.factory = factory;
    }

    @Override
    public void run(String... args) {
        Candidato candidato = new Candidato("c-001", factory.crear(NombreEstado.APLICADO));
        String autor = "reclutador-sofia";

        log.info("=== Hirecore: demostración de la arquitectura ===");
        log.info("Candidato inicial: {}", candidato);

        cambiar(candidato, NombreEstado.ENTREVISTA, autor);
        intentarSaltoInvalido(candidato, NombreEstado.CONTRATADO, autor);
        cambiar(candidato, NombreEstado.PRUEBA_TECNICA, autor);
        cambiar(candidato, NombreEstado.OFERTA, autor);
        cambiar(candidato, NombreEstado.CONTRATADO, autor);

        log.info("Deshaciendo el último cambio...");
        ejecutor.deshacer();
        log.info("Estado tras deshacer: {}", candidato);
        log.info("=== Fin de la demostración ===");
    }

    private void cambiar(Candidato candidato, NombreEstado destino, String autor) {
        ejecutor.ejecutar(new CambiarEstadoCommand(candidato, destino.name(), autor, factory));
        log.info("Estado actual: {}", candidato);
    }

    private void intentarSaltoInvalido(Candidato candidato, NombreEstado destino, String autor) {
        try {
            ejecutor.ejecutar(new CambiarEstadoCommand(candidato, destino.name(), autor, factory));
        } catch (TransicionEstadoInvalidaException ex) {
            log.warn("Transición rechazada por el estado: {}", ex.getMessage());
        }
    }
}
