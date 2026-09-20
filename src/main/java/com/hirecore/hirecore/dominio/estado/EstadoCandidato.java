package com.hirecore.hirecore.dominio.estado;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.excepcion.TransicionEstadoInvalidaException;

/**
 * Patrón State: cada estado conoce a cuáles otros puede transicionar.
 * La validación no vive en el comando ni en el candidato.
 */
public interface EstadoCandidato {

    String nombre();

    /**
     * Indica si desde este estado se puede pasar al destino.
     */
    boolean puedeTransicionar(EstadoCandidato nuevoEstado);

    /**
     * Aplica este estado al candidato si el estado actual lo permite.
     */
    default void avanzar(Candidato candidato) {
        EstadoCandidato actual = candidato.obtenerEstado();
        if (actual != null && !actual.puedeTransicionar(this)) {
            throw new TransicionEstadoInvalidaException(actual.nombre(), nombre());
        }
        candidato.cambiarEstado(this);
    }
}
