package com.hirecore.hirecore.dominio.estado;

import com.hirecore.hirecore.dominio.Candidato;
import com.hirecore.hirecore.dominio.excepcion.TransicionEstadoInvalidaException;

public interface EstadoCandidato {

    String nombre();

    boolean puedeTransicionar(EstadoCandidato nuevoEstado);

    default void avanzar(Candidato candidato) {
        EstadoCandidato actual = candidato.obtenerEstado();
        if (actual != null && !actual.puedeTransicionar(this)) {
            throw new TransicionEstadoInvalidaException(actual.nombre(), nombre());
        }
        candidato.cambiarEstado(this);
    }
}
