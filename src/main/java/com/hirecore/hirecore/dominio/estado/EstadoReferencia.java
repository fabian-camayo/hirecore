package com.hirecore.hirecore.dominio.estado;

public class EstadoReferencia extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.REFERENCIA.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return permite(nuevoEstado, NombreEstado.OFERTA, NombreEstado.RECHAZADO);
    }
}
