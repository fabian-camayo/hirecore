package com.hirecore.hirecore.dominio;

import com.hirecore.hirecore.dominio.estado.EstadoCandidato;

import java.util.Objects;

public class Candidato {

    private final String id;
    private EstadoCandidato estado;

    public Candidato(String id, EstadoCandidato estadoInicial) {
        this.id = Objects.requireNonNull(id, "id");
        this.estado = Objects.requireNonNull(estadoInicial, "estadoInicial");
    }

    public String getId() {
        return id;
    }

    public EstadoCandidato obtenerEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoCandidato nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado, "nuevoEstado");
    }

    @Override
    public String toString() {
        return "Candidato{id='%s', estado=%s}".formatted(id, estado.nombre());
    }
}
