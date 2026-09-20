package com.hirecore.hirecore.dominio.estado;

/**
 * Estado terminal: el candidato ya fue contratado.
 */
public class EstadoContratado extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.CONTRATADO.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return false;
    }
}
