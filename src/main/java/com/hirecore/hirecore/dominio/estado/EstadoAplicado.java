package com.hirecore.hirecore.dominio.estado;

/**
 * Primer estado del proceso. Solo puede pasar a entrevista o rechazo.
 * No permite saltar, por ejemplo, directo a contratado.
 */
public class EstadoAplicado extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.APLICADO.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return permite(nuevoEstado, NombreEstado.ENTREVISTA, NombreEstado.RECHAZADO);
    }
}
