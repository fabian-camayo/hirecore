package com.hirecore.hirecore.dominio.estado;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

abstract class EstadoBase implements EstadoCandidato {

    protected final boolean permite(EstadoCandidato destino, NombreEstado... permitidos) {
        Set<String> nombres = Arrays.stream(permitidos)
                .map(Enum::name)
                .collect(Collectors.toSet());
        return nombres.contains(destino.nombre());
    }

    @Override
    public final boolean equals(Object other) {
        return other instanceof EstadoCandidato estado && nombre().equals(estado.nombre());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(nombre());
    }

    @Override
    public final String toString() {
        return nombre();
    }
}
