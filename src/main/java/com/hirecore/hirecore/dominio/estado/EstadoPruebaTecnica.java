package com.hirecore.hirecore.dominio.estado;

public class EstadoPruebaTecnica extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.PRUEBA_TECNICA.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return permite(
                nuevoEstado,
                NombreEstado.REFERENCIA,
                NombreEstado.OFERTA,
                NombreEstado.RECHAZADO
        );
    }
}
