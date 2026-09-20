package com.hirecore.hirecore.dominio.estado;

public class EstadoEntrevista extends EstadoBase {

    @Override
    public String nombre() {
        return NombreEstado.ENTREVISTA.name();
    }

    @Override
    public boolean puedeTransicionar(EstadoCandidato nuevoEstado) {
        return permite(
                nuevoEstado,
                NombreEstado.PRUEBA_TECNICA,
                NombreEstado.REFERENCIA,
                NombreEstado.OFERTA,
                NombreEstado.RECHAZADO
        );
    }
}
