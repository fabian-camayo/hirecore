package com.hirecore.hirecore.dominio.estado;

public class EstadoRechazado extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.RECHAZADO.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return false;
    }
}
