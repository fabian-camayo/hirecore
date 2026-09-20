package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.estado.EstadoCandidato;
import com.hirecore.hirecore.dominio.estado.EstadoFactory;
import com.hirecore.hirecore.dominio.evento.CambioRevertido;
import com.hirecore.hirecore.dominio.evento.EstadoCambiado;
import com.hirecore.hirecore.dominio.evento.EventoDominio;

import java.util.Objects;

/**
 * Comando que cambia el estado de un candidato.
 * Guarda candidato, estado nuevo, estado anterior (para deshacer) y autor.
 * No valida la transición: eso lo hace el objeto estado al avanzar.
 */
public class CambiarEstadoCommand implements ComandoCandidato {

    private final Candidato candidato;
    private final EstadoCandidato nuevoEstado;
    private final EstadoFactory factory;
    private final String autor;
    private EstadoCandidato estadoAnterior;

    public CambiarEstadoCommand(Candidato candidato, String nombreNuevoEstado, String autor, EstadoFactory factory) {
        this.candidato = Objects.requireNonNull(candidato, "candidato");
        this.factory = Objects.requireNonNull(factory, "factory");
        this.nuevoEstado = factory.crear(nombreNuevoEstado);
        this.autor = Objects.requireNonNull(autor, "autor");
        this.estadoAnterior = candidato.obtenerEstado();
    }

    @Override
    public EventoDominio ejecutar() {
        estadoAnterior = candidato.obtenerEstado();
        nuevoEstado.avanzar(candidato);
        return new EstadoCambiado(
                candidato.getId(),
                estadoAnterior.nombre(),
                candidato.obtenerEstado().nombre(),
                autor
        );
    }

    @Override
    public EventoDominio deshacer() {
        if (estadoAnterior == null) {
            throw new IllegalStateException("No se puede deshacer un comando que aún no se ejecutó");
        }
        // Restaura sin revalidar: deshacer no es una transición de negocio.
        candidato.cambiarEstado(factory.crear(estadoAnterior.nombre()));
        return new CambioRevertido(candidato.getId(), candidato.obtenerEstado().nombre(), autor);
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public EstadoCandidato getNuevoEstado() {
        return nuevoEstado;
    }

    public String getAutor() {
        return autor;
    }
}
