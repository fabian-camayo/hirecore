package com.hirecore.hirecore.dominio.estado;

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
