package com.hirecore.hirecore.dominio.estado;

public class EstadoOferta extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.OFERTA.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return permite(nuevoEstado, NombreEstado.CONTRATADO, NombreEstado.RECHAZADO);
    }
}
